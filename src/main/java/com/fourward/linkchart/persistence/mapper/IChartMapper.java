package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.StockDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IChartMapper {
    // jsp 에 일자별 주식 가격 리스트 가져옴
    List<StockDTO> getStockData(StockDTO pDTO) throws Exception;

    int insertStockData(StockDTO pDTO) throws Exception;

    StockDTO getStockCodeByName(StockDTO pDTO) throws Exception;

    StockDTO getStockData_dateStart(StockDTO pDTO) throws Exception;

    StockDTO getStockData_dateEnd(StockDTO pDTO) throws Exception;
}
