package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.service.IChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChartController {
    private final IChartService chartService;

    @GetMapping(value = "/chart/getStockData")
    public List<StockDTO> getStockData(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".getStockData start");

        StockDTO pDTO = new StockDTO();

        // 종목명 & 검색 날짜벙위 입력. 날짜 Null 일때 기본값 = {시작날짜 : 오늘-2년, 종료날짜 : 오늘}
        pDTO.setName(request.getParameter("stockName"));
        pDTO.setStartDate_req(request.getParameter("startDate_req"));
        pDTO.setEndDate_req(request.getParameter("endDate_req"));
        log.info("requested stockName : " + pDTO.getName());
        log.info("requested startDate : " + pDTO.getStartDate_req());
        log.info("requested endDate : " + pDTO.getEndDate_req());

        List<StockDTO> rList = new ArrayList<>();
        try {
            // name 을 code 로 변환.
            pDTO.setCode((chartService.getStockCodeByName(pDTO)).getCode());

            // 입력된 데이터 날짜 범위 가져오기. 없으면 Null 값에 대한 예외처리.
            StockDTO dateRange = chartService.getStockData_dateRange(pDTO);
            try {
                pDTO.setStartDate_exist(dateRange.getStartDate_exist());
                pDTO.setEndDate_exist(dateRange.getEndDate_exist());
            } catch (NullPointerException ignored) {
            }
            /*
            추출한 코드로 데이터 크롤링 후 db 입력
            이후 db에서 데이터 가져옴
            */
            // 데이터 크롤링후 삽입. 중복 입력 방지 위한 기존 데이터 존재여부 검사.
            log.info(this.getClass().getName() + ".insertStockData start");

            StockDTO rDTO = chartService.insertStockData(pDTO);
            pDTO.setStartDate_req(rDTO.getStartDate_req());
            pDTO.setEndDate_req(rDTO.getEndDate_req());

            log.info(this.getClass().getName() + ".insertStockData end");

            // 데이터 가져오기 부분.
            rList = chartService.getStockData(pDTO);
        } catch (Exception e) {
            log.warn(this.getClass().getName() + "getStockData failed");
        }
        log.info(this.getClass().getName() + ".getStockData end");

        return rList;
    }
}
