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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("ChartService")
public class ChartService implements IChartService {

    private final IChartMapper chartMapper;

    public ChartService(IChartMapper chartMapper) {
        this.chartMapper = chartMapper;
    }

    @Override
    public List<StockDTO> getStockData() throws Exception {
        log.info(this.getClass().getName() + ".getStockData done");

        return chartMapper.getStockData();
    }

    @Override
    public void insertStockData(StockDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertStockData start");

        final String code = pDTO.getCode();
        final String start_date = pDTO.getStart_date();
        final String end_date = pDTO.getEnd_date();

        //api 크롤링 로직

        final String USER_AGENT = "Mozila/5.0";
        final String GET_URL = "https://api.finance.naver.com/siseJson.naver?symbol=" + code + "&requestType=1&startTime=" + start_date + "&endTime=" + end_date + "&timeframe=day";
        String json = "";
        try {
            //http client 생성
            CloseableHttpClient httpClient = HttpClients.createDefault();

            //get 메서드와 URL 설정
            HttpGet httpGet = new HttpGet(GET_URL);

            //agent 정보 설정
            httpGet.addHeader("User-Agent", USER_AGENT);
            httpGet.addHeader("Content-type", "application/json");

            //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            httpClient.close();
        } catch (ClientProtocolException e) {
            //예외처리 예정
        } catch (IOException e) {
        }
        //파싱부분
        String res = json.substring(json.indexOf("[", (json.indexOf("외국인"))), json.lastIndexOf("]"));
        res = res.trim().replaceAll("\\s", "").replaceAll("\"", "");
        res = res.substring(1, res.length() - 1);
        List<String> resList = Arrays.asList(res.split("],\\["));

        StockDTO tmpDTO;
        for (int i = 0; i < resList.size(); i++) {
            tmpDTO = new StockDTO();
            String[] tmpArr = resList.get(i).split(",");

            log.info("추출 날짜 : " + tmpArr[0]);
            tmpDTO.setCode(code);
            tmpDTO.setDate(tmpArr[0]);
            tmpDTO.setOpen(tmpArr[1]);
            tmpDTO.setHigh(tmpArr[2]);
            tmpDTO.setLow(tmpArr[3]);
            tmpDTO.setClose(tmpArr[4]);
            tmpDTO.setVolume(tmpArr[5]);
            chartMapper.insertStockData(tmpDTO);
            tmpDTO = null;
        }


        log.info(this.getClass().getName() + ".insertStockData end");
    }
}
