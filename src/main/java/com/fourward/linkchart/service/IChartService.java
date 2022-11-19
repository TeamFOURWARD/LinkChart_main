package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.StockDTO;

import java.util.List;

public interface IChartService {
    List<StockDTO> getStockData(StockDTO pDTO);

    void insertStockData(StockDTO pDTO) throws Exception;

    String getStockCodeByName(StockDTO pDTO);

    StockDTO getStockData_dateRange(StockDTO pDTO);

    StockDTO setDate(StockDTO pDTO) throws Exception;
}
