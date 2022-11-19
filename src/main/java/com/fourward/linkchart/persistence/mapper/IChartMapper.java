package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.StockDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IChartMapper {
    // jsp 에 일자별 주식 가격 리스트 가져옴
    List<StockDTO> getStockData(StockDTO pDTO);

    int insertStockData(StockDTO pDTO) throws Exception;

    String getStockCodeByName(StockDTO pDTO);

    String getStockData_dateStart(StockDTO pDTO);

    String getStockData_dateEnd(StockDTO pDTO);
}