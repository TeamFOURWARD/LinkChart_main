package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.service.IChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ChartController {

    @Resource(name = "ChartService")
    private IChartService chartService;

    @GetMapping(value = "/chart/searchStockData")
    public String searchStockName() throws Exception {

        return "/chart/searchStockData";
    }

    /*
     selected_code 코드, start_date 시작날짜.
     네이버 api 에 쿼리를 보낸후 받은 데이터를 DB 에 저장
     */
    @GetMapping(value = "/chart/insertStockData")
    public void insertStockData(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".insertStockData start");

        String code = request.getParameter("code");
        String start_date = request.getParameter("start_date");
        log.info("requested selected_code : " + code);
        log.info("requested start_date : " + start_date);

        StockDTO pDTO = new StockDTO();
        pDTO.setCode(code);
        pDTO.setStart_date(start_date);

        chartService.insertStockData(pDTO);

        log.info(this.getClass().getName() + ".insertStockData end");
    }

    @GetMapping(value = "/chart/getStockData")
    public String getStockData(HttpServletRequest request, ModelMap model) {

        return "/chart/viewStockChart";
    }
}
