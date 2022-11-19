package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.persistence.mapper.IChartMapper;
import com.fourward.linkchart.service.IChartService;
import com.fourward.linkchart.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Calendar.DATE;
import static java.util.Calendar.YEAR;

@Slf4j
@Service("ChartService")
@RequiredArgsConstructor
public class ChartService implements IChartService {
    private final IChartMapper chartMapper;

    @Override
    public List<StockDTO> getStockData(StockDTO rDTO) {

        return chartMapper.getStockData(rDTO);
    }

    @Transactional
    @Override
    public void insertStockData(StockDTO rDTO) throws Exception {
        @RequiredArgsConstructor
        class CrawlingStockData {
            private final String code;
            private final String startDate;
            private final String endDate;

            void run() throws Exception {
                final String USER_AGENT = "Mozila/5.0";
                final String GET_URL = "https://api.finance.naver.com/siseJson.naver?symbol=" + code + "&requestType=1&startTime=" + startDate + "&endTime=" + endDate + "&timeframe=day";

                log.info("crawling date range : [{}] ~ [{}]", startDate, endDate);
                // http client 생성
                CloseableHttpClient httpClient = HttpClients.createDefault();

                // get 메서드와 URL 설정
                HttpGet httpGet = new HttpGet(GET_URL);

                // agent 정보 설정
                httpGet.addHeader("User-Agent", USER_AGENT);
                httpGet.addHeader("Content-type", "application/json");

                // get 요청
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                httpClient.close();
                if (json.length() < 52) {
                    log.info("data does not exists.");

                    return;
                }
                // 파싱부분
                String res = json.substring(json.indexOf("[", (json.indexOf("외국인"))), json.lastIndexOf("]"));
                res = res.trim().replaceAll("\\s", "").replaceAll("\"", "");
                res = res.substring(1, res.length() - 1);
                String[] resList = res.split("],\\[");

                int insertedCount = 0;
                StockDTO tmpDTO;
                for (String s : resList) {
                    tmpDTO = new StockDTO();
                    String[] tmpArr = s.split(",");
                    tmpDTO.setCode(code);
                    tmpDTO.setDate(tmpArr[0]);
                    tmpDTO.setOpen(tmpArr[1]);
                    tmpDTO.setHigh(tmpArr[2]);
                    tmpDTO.setLow(tmpArr[3]);
                    tmpDTO.setClose(tmpArr[4]);
                    tmpDTO.setVolume(tmpArr[5]);
                    insertedCount += chartMapper.insertStockData(tmpDTO);
                }
                log.info("insertedCount : {}", insertedCount);
            }
        }
        //메인 로직
        log.info("{}.insertStockData start", this.getClass().getName());
        /*
        검색 시작일 = start_req
        검색 종료일 = end_req
        이미 존재하는 데이터 시작일 = start_exist
        이미 존재하는 데이터 종료일 = end_exist

        start_exist 가 널 이면 end_exist 도 널, start_exist-=2년, end_exist=오늘

        if (start_exist)-(start_req) > 0 :
            (start_req) ~ (start_exist-1일) 크롤링후 db 삽입 (case1)
        if (end_req)-(start_exist) > 0 :
            (end_exist+1일) ~ (end_req) 크롤링후 db 삽입 (case2)
        */
        if (rDTO.getStartDate_exist().equals("")) {
            new CrawlingStockData(rDTO.getCode(), rDTO.getStartDate_req(), rDTO.getEndDate_req()).run();
        } else {
            if (DateUtil.compare(rDTO.getStartDate_exist(), rDTO.getStartDate_req()) > 0) {
                new CrawlingStockData(rDTO.getCode(), rDTO.getStartDate_req(), DateUtil.changeDate(rDTO.getStartDate_exist(), DATE, -1)).run();
            }
            if (DateUtil.compare(rDTO.getEndDate_req(), rDTO.getStartDate_exist()) > 0) {
                new CrawlingStockData(rDTO.getCode(), DateUtil.changeDate(rDTO.getEndDate_exist(), DATE, +1), rDTO.getEndDate_req()).run();
            }
        }
        log.info("{}.insertStockData end", this.getClass().getName());
    }

    @Override
    public String getStockCodeByName(StockDTO pDTO) {
        log.info("{}.getStockCodeByName start", this.getClass().getName());

        String code = chartMapper.getStockCodeByName(pDTO);
        if (code == null) {
            code = "";
        }

        log.info("{}.getStockCodeByName end", this.getClass().getName());

        return code;
    }

    @Override
    public StockDTO getStockData_dateRange(StockDTO rDTO) {
        log.info("{}.getStockDate_dateRange start", this.getClass().getName());

        String dateStart = chartMapper.getStockData_dateStart(rDTO);
        String dateEnd = chartMapper.getStockData_dateEnd(rDTO);
        if (dateStart == null) {
            rDTO.setStartDate_exist("");
        } else {
            rDTO.setStartDate_exist(dateStart);
        }
        if (dateEnd == null) {
            rDTO.setEndDate_exist("");
        } else {
            rDTO.setEndDate_exist(dateEnd);
        }

        log.info("{}.getStockDate_dateRange end", this.getClass().getName());

        return rDTO;
    }

    @Override
    public StockDTO setDate(StockDTO pDTO) throws Exception {
        // 사용자 검색 날짜
        if (pDTO.getEndDate_req().equals("")) {
            pDTO.setEndDate_req(DateUtil.getNowDate());
        }
        if (pDTO.getStartDate_req().equals("")) {
            pDTO.setStartDate_req(DateUtil.changeDate(pDTO.getEndDate_req(), YEAR, -2));
        }
        return pDTO;
    }
}
