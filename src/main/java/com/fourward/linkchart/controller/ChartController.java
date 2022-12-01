package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.StockInfoDTO;
import com.fourward.linkchart.dto.StockReqDTO;
import com.fourward.linkchart.service.IChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/chart")
@RequiredArgsConstructor
public class ChartController {
    private final IChartService chartService;

    @PostMapping(value = "/getStockData")
    public ResponseEntity<List<StockInfoDTO>> getStockData(@RequestBody StockReqDTO pDTO) {
        log.info("{}.getStockData start", this.getClass().getName());
        // 종목명 & 검색 날짜범위 입력. 날짜 Null 일때 기본값 = {시작날짜 : 오늘-2년, 종료날짜 : 오늘}
        log.info("stockName requested : [{}]", pDTO.getName());
        log.info("startDate requested : [{}]", pDTO.getStartTime());
        log.info("endDate requested : [{}]", pDTO.getEndTime());
        log.info("timeframe requested : [{}]", pDTO.getTimeframe());
        // 종목 코드와 날짜 범위를 구함
        try {
            StockReqDTO rDTO = chartService.setCondition(pDTO);
            pDTO.setSymbol(rDTO.getSymbol());
            log.info("code requested : [{}]", pDTO.getSymbol());
            if (pDTO.getSymbol().equals("")) {
                log.info("invalid stock name. return null.");
                log.info("{}.getStockData end", this.getClass().getName());
                // 클라이언트 에러
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            pDTO.setStartTime(rDTO.getStartTime());
            pDTO.setEndTime(rDTO.getEndTime());
        } catch (ParseException e) {
            // 서버 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 데이터 삽입, 데이터 가져오기
        List<StockInfoDTO> rList;
        try {
            chartService.insertStockData(pDTO);
            rList = chartService.getStockData(pDTO);
        } catch (URISyntaxException | MalformedURLException | ParseException e) {
            // 서버 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // 서버 에러
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("{}.getStockData end", this.getClass().getName());
        // TODO 헤더 첨가
        return new ResponseEntity<>(rList, HttpStatus.OK);
    }
}
