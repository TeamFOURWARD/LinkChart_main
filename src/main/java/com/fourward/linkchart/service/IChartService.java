package com.fourward.linkchart.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fourward.linkchart.dto.StockDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public interface IChartService {

    JSONArray getStockData(StockDTO pDTO) throws Exception;

    void insertStockData(StockDTO pDTO) throws Exception;

    StockDTO getStockCodeByName(StockDTO pDTO) throws Exception;
}
