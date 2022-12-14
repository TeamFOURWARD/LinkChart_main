package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.StockInfoDTO;
import com.fourward.linkchart.dto.StockReqDTO;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

public interface IChartService {
    StockReqDTO setCondition(StockReqDTO pDTO) throws ParseException;

    void insertStockData(StockReqDTO pDTO) throws URISyntaxException, MalformedURLException, ParseException;

    List<StockInfoDTO> getStockData(StockReqDTO pDTO) throws Exception;
}