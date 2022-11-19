package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.StockDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IChartMapper {
    List<StockDTO> getStockData(StockDTO pDTO);

    int insertStockData(StockDTO pDTO);

    String getStockCodeByName(StockDTO pDTO);

    String getStockData_dateStart(StockDTO pDTO);

    String getStockData_dateEnd(StockDTO pDTO);
}