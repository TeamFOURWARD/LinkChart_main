package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.StockInfoDTO;
import com.fourward.linkchart.dto.StockReqDTO;
import com.fourward.linkchart.persistence.mapper.IChartMapper;
import com.fourward.linkchart.service.IChartService;
import com.fourward.linkchart.util.DateUtil;
import com.fourward.linkchart.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

import static java.util.Calendar.DATE;
import static java.util.Calendar.YEAR;

@Slf4j
@Service("ChartService")
@RequiredArgsConstructor
public class ChartService implements IChartService {
    private final IChartMapper chartMapper;

    @Override
    public StockReqDTO setCondition(StockReqDTO pDTO) throws ParseException {
        if (pDTO.getEndTime().equals("")) {
            pDTO.setEndTime(DateUtil.getNowDate());
        }
        if (pDTO.getStartTime().equals("")) {
            pDTO.setStartTime(DateUtil.changeDate(pDTO.getEndTime(), YEAR, -2));
        }
        String symbol = chartMapper.getStockCodeByName(pDTO);
        if (symbol == null) {
            symbol = "";
        }
        pDTO.setSymbol(symbol);

        return pDTO;
    }

    @Override
    @Transactional
    public void insertStockData(StockReqDTO rDTO) throws URISyntaxException, MalformedURLException, ParseException {
        log.info("{}.insertStockData start", this.getClass().getName());

        String startTime_exist = chartMapper.getStockData_dateStart(rDTO);
        String endTime_exist = chartMapper.getStockData_dateEnd(rDTO);
        String startTime = rDTO.getStartTime();
        String endTime = rDTO.getEndTime();
        String symbol = rDTO.getSymbol();
        String timeframe = rDTO.getTimeframe();
        log.info("date range of stock_data already exists : [{}] ~ [{}]", startTime_exist, endTime_exist);
        /*
        검색 시작일 = startTime
        검색 종료일 = endTime
        이미 존재하는 데이터 시작일 = startTime_exist
        이미 존재하는 데이터 종료일 = endTime_exist

        startTime_exist 가 널 이면 endTime_exist 도 널, startTime_exist-=2년, endTime_exist=오늘

        if (startTime_exist == null):
            (startTime) ~ (endTime) 크롤링후 db 삽입
            if (startTime_exist)-(startTime) > 0 :
                (startTime) ~ (startTime_exist-1일) 크롤링후 db 삽입
            if (endTime_req)-(startTime_exist) > 0 :
                (endTime_exist+1일) ~ (endTime_req) 크롤링후 db 삽입
        */
        List<String> list = new ArrayList<>();
        {// TODO 중복코드 개선
            URIBuilder uriBuilder;
            URL url;
            Map<String, String> header = new HashMap<>();
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
            header.put("Content-type", "application/json;charset=utf-8");
            if (startTime_exist == null) {
                uriBuilder = new URIBuilder();
                uriBuilder.setHost("api.finance.naver.com");
                uriBuilder.setScheme("http");
                uriBuilder.setCharset(StandardCharsets.UTF_8);
                uriBuilder.setPath("/siseJson.naver");
                uriBuilder.setParameter("requestType", "1");

                uriBuilder.setParameter("symbol", symbol);
                uriBuilder.setParameter("startTime", startTime);
                uriBuilder.setParameter("endTime", endTime);
                uriBuilder.setParameter("timeframe", timeframe);
                url = uriBuilder.build().toURL();
                String s = NetworkUtil.get(url, header);
                if (s.length() > 55) {
                    String[] resList = s.substring(44, s.length() - 2).split("],\\[");
                    list.addAll(Arrays.asList(resList));
                }
            } else {
                if (DateUtil.compare(startTime_exist, startTime) > 0) {
                    uriBuilder = new URIBuilder();
                    uriBuilder.setHost("api.finance.naver.com");
                    uriBuilder.setScheme("http");
                    uriBuilder.setCharset(StandardCharsets.UTF_8);
                    uriBuilder.setPath("/siseJson.naver");
                    uriBuilder.setParameter("requestType", "1");

                    uriBuilder.setParameter("symbol", symbol);
                    uriBuilder.setParameter("startTime", startTime);
                    uriBuilder.setParameter("endTime", DateUtil.changeDate(startTime_exist, DATE, -1));
                    uriBuilder.setParameter("timeframe", timeframe);
                    url = uriBuilder.build().toURL();
                    String s = NetworkUtil.get(url, header);
                    if (s.length() > 55) {
                        String[] resList = s.substring(44, s.length() - 2).split("],\\[");
                        list.addAll(Arrays.asList(resList));
                    }
                }
                if (DateUtil.compare(endTime, endTime_exist) > 0) {
                    uriBuilder = new URIBuilder();
                    uriBuilder.setHost("api.finance.naver.com");
                    uriBuilder.setScheme("http");
                    uriBuilder.setCharset(StandardCharsets.UTF_8);
                    uriBuilder.setPath("/siseJson.naver");
                    uriBuilder.setParameter("requestType", "1");

                    uriBuilder.setParameter("symbol", symbol);
                    uriBuilder.setParameter("startTime", DateUtil.changeDate(endTime_exist, DATE, +1));
                    uriBuilder.setParameter("endTime", endTime);
                    uriBuilder.setParameter("timeframe", timeframe);
                    url = uriBuilder.build().toURL();
                    String s = NetworkUtil.get(url, header);
                    if (s.length() > 55) {
                        String[] resList = s.substring(44, s.length() - 2).split("],\\[");
                        list.addAll(Arrays.asList(resList));
                    }
                }
            }
        }
        int insertCount = 0;
        StockInfoDTO stockInfoDTO;
        for (String s : list) {
            stockInfoDTO = new StockInfoDTO();
            String[] arr = s.split(",");
            stockInfoDTO.setCode(symbol);
            stockInfoDTO.setTimeframe(timeframe);
            stockInfoDTO.setDate(arr[0].substring(1, 9));
            stockInfoDTO.setOpen(arr[1]);
            stockInfoDTO.setHigh(arr[2]);
            stockInfoDTO.setLow(arr[3]);
            stockInfoDTO.setClose(arr[4]);
            stockInfoDTO.setVolume(arr[5]);
            chartMapper.insertStockData(stockInfoDTO);
            insertCount++;
        }
        log.info("insertCount : [{}]", insertCount);
        log.info("{}.insertStockData end", this.getClass().getName());
    }

    @Override
    public List<StockInfoDTO> getStockData(StockReqDTO pDTO) throws Exception {
        return chartMapper.getStockData(pDTO);
    }
}