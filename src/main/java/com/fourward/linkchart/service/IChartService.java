package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.StockDTO;

import java.util.List;

public interface IChartService {

    List<StockDTO> getStockData() throws Exception;

    void insertStockData(StockDTO pDTO) throws Exception;
}
