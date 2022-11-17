package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.service.IChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/chart")
@RequiredArgsConstructor
public class ChartController {
    private final IChartService chartService;

    @PostMapping(value = "/getStockData")
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

        List<StockDTO> rList;
        // name 을 code 로 변환.
        pDTO.setCode((chartService.getStockCodeByName(pDTO)).getCode());
        if (pDTO.getCode().equals("")) {
            // 예외. 찾는 이름이 없음.
            return null;
        }

        // 입력된 데이터 날짜 범위 가져오기. 없으면 Null 값에 대한 예외처리.
        StockDTO dateRange = chartService.getStockData_dateRange(pDTO);
        if (!dateRange.getStartDate_exist().equals("")) {
            pDTO.setStartDate_exist(dateRange.getStartDate_exist());
        }
        if (!dateRange.getEndDate_exist().equals("")) {
            pDTO.setEndDate_exist(dateRange.getEndDate_exist());
        }

        // 데이터 크롤링후 삽입. 중복 입력 방지 위한 기존 데이터 존재여부 검사.
        StockDTO rDTO;
        try {
            rDTO = chartService.insertStockData(pDTO);
            // DateUtil 에서 발생. 예외가능성 거의 없음.
        } catch (ParseException e) {
            log.info(this.getClass().getName() + " | DateUtil error");
            log.info(this.getClass().getName() + ".getStockData end");

            return null;
        }

        pDTO.setStartDate_req(rDTO.getStartDate_req());
        pDTO.setEndDate_req(rDTO.getEndDate_req());

        // 데이터 가져오기.
        rList = chartService.getStockData(pDTO);
        log.info(this.getClass().getName() + ".getStockData end");

        return rList;
    }
}
