package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.StockInfoDTO;
import com.fourward.linkchart.dto.StockReqDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IChartMapper {
    void insertStockData(StockInfoDTO pDTO);

    List<StockInfoDTO> getStockData(StockReqDTO pDTO) throws Exception;

    String getStockCodeByName(StockReqDTO pDTO);

    String getStockData_dateStart(StockReqDTO pDTO);

    String getStockData_dateEnd(StockReqDTO pDTO);
}