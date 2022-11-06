package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.persistence.mapper.IChartMapper;
import com.fourward.linkchart.service.IChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("ChartService")
@RequiredArgsConstructor
public class ChartService implements IChartService {
    private final IChartMapper chartMapper;

    @Override
    public List<StockDTO> getStockData(StockDTO rDTO) throws Exception {

        return chartMapper.getStockData(rDTO);
    }

    @Transactional
    @Override
    public StockDTO insertStockData(StockDTO rDTO) throws Exception {
        @RequiredArgsConstructor
        class CrawlingStockData {
            private final String code;
            private final String startDate;
            private final String endDate;

            void get() throws Exception {
                log.info(this.getClass().getName() + ".insertData start");
                final String USER_AGENT = "Mozila/5.0";
                final String GET_URL = "https://api.finance.naver.com/siseJson.naver?symbol=" + code
                        + "&requestType=1&startTime=" + startDate + "&endTime=" + endDate + "&timeframe=day";

                log.info("code : " + code);
                log.info("start_date : " + startDate);
                log.info("end_date : " + endDate);
                log.info("GET_URL : " + GET_URL);

                String json = "";
                try {
                    // http client 생성
                    CloseableHttpClient httpClient = HttpClients.createDefault();

                    // get 메서드와 URL 설정
                    HttpGet httpGet = new HttpGet(GET_URL);

                    // agent 정보 설정
                    httpGet.addHeader("User-Agent", USER_AGENT);
                    httpGet.addHeader("Content-type", "application/json");

                    // get 요청
                    CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

                    json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

                    httpClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.warn("error : " + e);
                }

                // 파싱부분
                try {
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
                    log.info("insertedCount : " + insertedCount);
                } catch (Exception e) {
                    log.info(this.getClass().getName() + " 추출 데이터 없음. 데이터 입력 종료.\n");
                }
                log.info(this.getClass().getName() + ".insertData end");
            }
        }
        @RequiredArgsConstructor
        class DateCompare {
            private final String date1;
            private final String date2;

            long get() throws ParseException {
                Date format1 = new SimpleDateFormat("yyyyMMdd").parse(date1);
                Date format2 = new SimpleDateFormat("yyyyMMdd").parse(date2);

                return format1.getTime() - format2.getTime();
            }
        }
        @RequiredArgsConstructor
        class CalDate {
            private final String date;
            private final int k;

            String get() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Calendar c1 = Calendar.getInstance();
                try {
                    Date date1 = sdf.parse(date);
                    c1.setTime(date1);
                    c1.add(Calendar.DATE, k);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return sdf.format(c1.getTime());
            }
        }

        //메인 로직
        log.info(this.getClass().getName() + ".insertStockData start");

        // 사용자 검색 날짜 널값 처리
        String ifNull_start_req = rDTO.getStartDate_req();
        String ifNull_end_req = rDTO.getEndDate_req();
        if (Objects.equals(ifNull_end_req, "")) {
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            ifNull_end_req = now.format(formatter);
        }
        if (Objects.equals(ifNull_start_req, "")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar c = Calendar.getInstance();
            try {
                Date date = sdf.parse(ifNull_end_req);
                c.setTime(date);
                c.add(Calendar.YEAR, -2);// 요청된 검색날짜 시작일이 없을시 2년 전으로.
            } catch (ParseException e) {
                e.printStackTrace();
                throw new Exception();
            }
            ifNull_start_req = sdf.format(c.getTime());
        }

        final String code = rDTO.getCode();
        final String start_req = ifNull_start_req;
        final String start_exist = rDTO.getStartDate_exist();
        final String end_req = ifNull_end_req;
        final String end_exist = rDTO.getEndDate_exist();

        log.info(this.getClass().getName() + "\nstartDate_req : " + start_req);
        log.info(this.getClass().getName() + "\nendDate_req : " + end_req);
        log.info(this.getClass().getName() + "\nstartDate_exist : " + start_exist);
        log.info(this.getClass().getName() + "\nendDate_exist : " + end_exist);
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
            new CrawlingStockData(code, start_req, end_req).get();
        } else {
            if (new DateCompare(start_exist, start_req).get() > 0) {
                new CrawlingStockData(code, start_req, new CalDate(start_exist, -1).get()).get();
            }
            if (new DateCompare(end_req, start_exist).get() > 0) {
                new CrawlingStockData(code, new CalDate(end_exist, +1).get(), end_req).get();
            }
        }
        rDTO.setStartDate_req(start_req);
        rDTO.setEndDate_req(end_req);

        log.info(this.getClass().getName() + ".insertStockData end");

        return rDTO;
    }

    @Override
    public StockDTO getStockCodeByName(StockDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getStockCodeByName start");

        StockDTO code = chartMapper.getStockCodeByName(pDTO);
        try {
            if (Objects.equals(code.getCode(), null)) {
                throw new Exception();
            }
        } catch (Exception e) {
            log.warn("이름 잘못 입력");
        }
        log.info(this.getClass().getName() + ".getStockCodeByName end");

        return code;
    }

    @Override
    public StockDTO getStockData_dateRange(StockDTO rDTO) throws Exception {
        log.info(this.getClass().getName() + ".getStockDate_dateRange start");

        try {
            rDTO.setStartDate_exist((chartMapper.getStockData_dateStart(rDTO)).getStartDate_exist());
            log.info("startDate_exist : " + rDTO.getStartDate_exist());
        } catch (NullPointerException e) {
            log.info("startDate_exist : null");
        }
        try {
            rDTO.setEndDate_exist((chartMapper.getStockData_dateEnd(rDTO)).getEndDate_exist());
            log.info("endDate_exist : " + rDTO.getEndDate_exist());
        } catch (NullPointerException e) {
            log.info("endDate_exist : null");
        }
        log.info(this.getClass().getName() + ".getStockDate_dateRange end");

        return rDTO;
    }
}
