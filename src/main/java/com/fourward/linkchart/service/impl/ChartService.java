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

import java.io.IOException;
import java.text.ParseException;
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
    public StockDTO insertStockData(StockDTO rDTO) throws ParseException {
        @RequiredArgsConstructor
        class InsertData {
            private final String code;
            private final String startDate;
            private final String endDate;

            int run() {
                log.info(this.getClass().getName() + ".insertData start");
                final String USER_AGENT = "Mozila/5.0";
                final String GET_URL = "https://api.finance.naver.com/siseJson.naver?symbol=" + code + "&requestType=1&startTime=" + startDate + "&endTime=" + endDate + "&timeframe=day";

                log.info("code : " + code);
                log.info("start_date : " + startDate);
                log.info("end_date : " + endDate);
                log.info("GET_URL : " + GET_URL);

                // http client 생성
                CloseableHttpClient httpClient = HttpClients.createDefault();

                // get 메서드와 URL 설정
                HttpGet httpGet = new HttpGet(GET_URL);

                // agent 정보 설정
                httpGet.addHeader("User-Agent", USER_AGENT);
                httpGet.addHeader("Content-type", "application/json");

                String json = "";
                try {
                    // get 요청
                    CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                    httpClient.close();

                    json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                } catch (IOException e) {
                    // 크롤링시 발생한 에러
                    log.info(this.getClass().getName() + ".insertData end");

                    return -1;
                }

                if (json.length() < 52) {
                    log.info(this.getClass().getName() + " | data not exists");
                    log.info(this.getClass().getName() + ".insertData end");
                    // insert 할 데이터가 없음.

                    return 0;
                }

                // 파싱
                String res = json.substring(json.indexOf("[", (json.indexOf("외국인"))), json.lastIndexOf("]"));
                res = res.trim().replaceAll("\\s", "").replaceAll("\"", "");
                res = res.substring(1, res.length() - 1);
                String[] resList = res.split("],\\[");

                int insertedCount = 0;

                StockDTO tmpDTO;
                for (String s : resList) {
                    tmpDTO = new StockDTO();
                    String[] tmpArr = s.split(",");

                    log.info("추출 날짜 : " + tmpArr[0]);
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
                log.info(this.getClass().getName() + ".insertData end");

                return 1;
            }
        }
        //메인 로직
        log.info(this.getClass().getName() + ".insertStockData start");

        // 사용자 검색 날짜 널값 처리
        String ifNull_start_req = rDTO.getStartDate_req();
        String ifNull_end_req = rDTO.getEndDate_req();
        if (ifNull_end_req.equals("")) {
            ifNull_end_req = DateUtil.getNowDate();
        }
        if (ifNull_start_req.equals("")) {
            ifNull_start_req = DateUtil.date(ifNull_end_req, YEAR, -2);
        }
        final String code = rDTO.getCode();
        final String start_req = ifNull_start_req;
        final String start_exist = rDTO.getStartDate_exist();
        final String end_req = ifNull_end_req;
        final String end_exist = rDTO.getEndDate_exist();
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
        if (start_exist == null) {
            new InsertData(code, start_req, end_req);
        } else {
            if (DateUtil.compare(start_exist, start_req) > 0) {
                int t = new InsertData(code, start_req, DateUtil.date(start_exist, DATE, -1)).run();
                if (t == -1) {
                    // 크롤링 및 데이터 입력 런타임 에러.
                    return null;
                }
            }
            if (DateUtil.compare(end_req, start_exist) > 0) {
                int t = new InsertData(code, DateUtil.date(end_exist, DATE, +1), end_req).run();
                if (t == -1) {
                    // 크롤링 및 데이터 입력 런타임 에러.
                    return null;
                }
            }
        }
        rDTO.setStartDate_req(start_req);
        rDTO.setEndDate_req(end_req);

        log.info(this.getClass().getName() + ".insertStockData end");

        return rDTO;
    }

    @Override
    public StockDTO getStockCodeByName(StockDTO pDTO) {
        log.info(this.getClass().getName() + ".getStockCodeByName start");

        StockDTO code = chartMapper.getStockCodeByName(pDTO);

        log.info(this.getClass().getName() + ".getStockCodeByName end");

        return code;
    }

    @Override
    public StockDTO getStockData_dateRange(StockDTO rDTO) {
        log.info(this.getClass().getName() + ".getStockDate_dateRange start");

        rDTO.setStartDate_exist((chartMapper.getStockData_dateStart(rDTO)).getStartDate_exist());
        log.info("startDate_exist : {}", rDTO.getStartDate_exist());
        rDTO.setEndDate_exist((chartMapper.getStockData_dateEnd(rDTO)).getEndDate_exist());
        log.info("endDate_exist : {}", rDTO.getEndDate_exist());

        log.info(this.getClass().getName() + ".getStockDate_dateRange end");

        return rDTO;
    }
}
