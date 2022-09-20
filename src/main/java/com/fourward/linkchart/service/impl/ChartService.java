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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service("ChartService")
public class ChartService implements IChartService {

    private final IChartMapper chartMapper;

    public ChartService(IChartMapper chartMapper) {
        this.chartMapper = chartMapper;
    }

    @Override
    public JSONArray getStockData(StockDTO rDTO) throws Exception {
        log.info(this.getClass().getName() + ".getStockData start");

        List<StockDTO> rList = chartMapper.getStockData(rDTO);

        JSONArray jsonList = new JSONArray();
        for (StockDTO stockDTO : rList) {
            JSONObject json = new JSONObject();
            json.put("date", Integer.parseInt(stockDTO.getDate()));
            json.put("low", Integer.parseInt(stockDTO.getLow()));
            json.put("high", Integer.parseInt(stockDTO.getHigh()));
            json.put("open", Integer.parseInt(stockDTO.getOpen()));
            json.put("close", Integer.parseInt(stockDTO.getClose()));
            jsonList.add(json);
        }

        return jsonList;
    }

    @Transactional
    @Override
    public void insertStockData(StockDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertStockData start");

        // api 크롤링 로직 1 [전날까지]

        final String code = pDTO.getCode();
        final String start_date = pDTO.getStart_date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, -1);
        final String yesterday = sdf.format(c1.getTime());

        final String USER_AGENT = "Mozila/5.0";
        final String GET_URL = "https://api.finance.naver.com/siseJson.naver?symbol=" + code
                + "&requestType=1&startTime=" + start_date + "&endTime=" + yesterday + "&timeframe=day";

        log.info("code : " + code);
        log.info("start_date : " + start_date);
        log.info("end_date : " + yesterday);
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
        } catch (ClientProtocolException e) {
            // 예외처리 예정
        } catch (IOException e) {
        } catch (Exception e) {
            log.debug("error : " + e);
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
        log.info(this.getClass().getName() + ".insertStockData end");
    }

    @Override
    public StockDTO getStockCodeByName(StockDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getStockCodeByName start");
        return chartMapper.getStockCodeByName(pDTO);
    }

}
