package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.service.IChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/chart")
@RequiredArgsConstructor
public class ChartController {
    private final IChartService chartService;

    @PostMapping(value = "/getStockData")
    public List<StockDTO> getStockData(HttpServletRequest request) {
        log.info("{}.getStockData start", this.getClass().getName());

        StockDTO pDTO = new StockDTO();

        // 종목명 & 검색 날짜범위 입력. 날짜 Null 일때 기본값 = {시작날짜 : 오늘-2년, 종료날짜 : 오늘}
        pDTO.setName(request.getParameter("stockName"));
        pDTO.setStartDate_req(request.getParameter("startDate_req"));
        pDTO.setEndDate_req(request.getParameter("endDate_req"));
        log.info("requested stockName : [{}]", pDTO.getName());
        log.info("requested startDate : [{}]", pDTO.getStartDate_req());
        log.info("requested endDate : [{}]", pDTO.getEndDate_req());

        // name 을 code 로 변환.
        pDTO.setCode((chartService.getStockCodeByName(pDTO)));
        log.info("requested code : [{}]", pDTO.getCode());
        if (pDTO.getCode().equals("")) {
            log.info("invalid stock name. return null.");
            log.info(this.getClass().getName() + ".getStockData end");

            return null;
        }

        // 입력된 데이터 날짜 범위 가져오기.
        StockDTO dateRange = chartService.getStockData_dateRange(pDTO);
        if (dateRange.getStartDate_exist().equals("")) {
            pDTO.setStartDate_exist("");
        } else {
            pDTO.setStartDate_exist(dateRange.getStartDate_exist());
        }
        if (dateRange.getEndDate_exist().equals("")) {
            pDTO.setEndDate_exist("");
        } else {
            pDTO.setEndDate_exist(dateRange.getEndDate_exist());
        }
        log.info("date range of stock_data that already exists : [{}] ~ [{}]", pDTO.getStartDate_exist(), pDTO.getEndDate_exist());

        try {
            // 데이터 가져오기 날짜 범위 설정
            pDTO.setStartDate_req((chartService.setDate(pDTO)).getStartDate_req());
            pDTO.setEndDate_req((chartService.setDate(pDTO)).getEndDate_req());
            chartService.insertStockData(pDTO);
        } catch (Exception ignored) {
            // 유틸 클래스 관련 IOException, ParseException, HTTPClient 관련 예외.
            log.info("{}.getStockData end", this.getClass().getName());

            return null;
        }
        log.info("{}.getStockData end", this.getClass().getName());
        
        HashMap<String, Object> hashMap = new HashMap<>();
        return chartService.getStockData(pDTO);
    }
}
