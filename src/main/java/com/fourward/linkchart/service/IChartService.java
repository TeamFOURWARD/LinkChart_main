package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.StockDTO;

import java.text.ParseException;
import java.util.List;

public interface IChartService {
    List<StockDTO> getStockData(StockDTO pDTO) ;

    StockDTO insertStockData(StockDTO pDTO) throws ParseException ;

    StockDTO getStockCodeByName(StockDTO pDTO) ;

    StockDTO getStockData_dateRange(StockDTO pDTO) ;
}
