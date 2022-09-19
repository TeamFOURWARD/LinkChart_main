package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.service.IChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ChartController {

    @Resource(name = "ChartService")
    private IChartService chartService;

    /**
     * selected_name 종목명, start_date 시작날짜, end_date 종료날짜 로
     * 네이버 api 에 쿼리를 보낸후 받은 데이터를 DB 에 저장
     */

    @GetMapping(value = "/chart/insertStockData")
    public void insertStockData(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".insertStockData start");

        StockDTO pDTO = new StockDTO();
        //name 을 code 로 변환

        String name = request.getParameter("name");
        pDTO.setName(name);
        pDTO.setCode((chartService.getStockCodeByName(pDTO)).getCode());
        log.info("selected_name : " + pDTO.getName());
        log.info("selected_code : " + pDTO.getCode());
        //이름 잘못 입력시 에러처리 필요

        String start_date = request.getParameter("start_date");
        log.info("start_date : " + start_date);

        pDTO.setStart_date(start_date);

        chartService.insertStockData(pDTO);


        log.info(this.getClass().getName() + ".insertStockData end");
    }

    @GetMapping(value = "/chart/searchStockData")
    public String getStockNameByCode() throws Exception {


        log.info(this.getClass().getName() + ".getStockNameByCode end");

        return "/chart/searchStockData";
    }
    /*
    종목명 입력시
     */
    @GetMapping(value = "/chart/getStockData")
    public String getStockData(HttpServletRequest request,ModelMap model) throws Exception {
        log.info(this.getClass().getName()+".getStockData start");
        String name = request.getParameter("name");

        StockDTO pDTO = new StockDTO();
        pDTO.setName(name);

        chartService.getChartData(pDTO);


        List<StockDTO> pList = new ArrayList<>();

        model.getAttribute(name);

        log.info(this.getClass().getName()+".getStockData end");

        return "/chart/viewStockChart";
    }
}
