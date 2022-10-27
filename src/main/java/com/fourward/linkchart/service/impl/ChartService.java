package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.persistence.mapper.IChartMapper;
import com.fourward.linkchart.service.IChartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("ChartService")
public class ChartService implements IChartService {

    private final IChartMapper chartMapper;

    @Autowired
    public ChartService(IChartMapper chartMapper) {
        this.chartMapper = chartMapper;
    }

    @Override
    public List<StockDTO> getStockData(StockDTO rDTO) throws Exception {
        log.info(this.getClass().getName() + ".getStockData start");

        return chartMapper.getStockData(rDTO);
    }

    @Transactional
    @Override
    public void insertStockData(StockDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertStockData start");

        //크롤링 로직 1 [전날까지]

        final String code = pDTO.getCode();
        final String start_date = pDTO.getStart_date();
        String end_date = pDTO.getEnd_date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (end_date == null) {
            //현재날짜 -1 로
            Calendar c1 = Calendar.getInstance();

            c1.add(Calendar.DATE, -1);

            end_date = sdf.format(c1.getTime());
        } else {
            //가져온 end_date -1 로
            Date date = null;
            try {
                date = sdf.parse(end_date);
            } catch (ParseException e) {
                log.debug(e.getMessage());
            }
            Calendar c1 = Calendar.getInstance();
            assert date != null;
            c1.setTime(date);

            c1.add(Calendar.DATE, -1);

            end_date = sdf.format(c1.getTime());
        }

        final String USER_AGENT = "Mozila/5.0";
        final String GET_URL = "https://api.finance.naver.com/siseJson.naver?symbol=" + code
                + "&requestType=1&startTime=" + start_date + "&endTime=" + end_date + "&timeframe=day";

        log.info("code : " + code);
        log.info("start_date : " + start_date);
        log.info("end_date : " + end_date);
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
            log.debug("error : " + e);
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
                tmpDTO = null;
            }
            log.info("insertedCount : " + insertedCount);
        } catch (StringIndexOutOfBoundsException e) {
            log.info("추출된 데이터 없음. 데이터 입력 종료.");
        }
        log.info(this.getClass().getName() + ".insertStockData end");
    }

    @Override
    public StockDTO getStockCodeByName(StockDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getStockCodeByName start");

        return chartMapper.getStockCodeByName(pDTO);
    }

}
