package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.StockDTO;
import com.fourward.linkchart.service.IChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
public class ChartController {

    private final IChartService chartService;

    @Autowired
    public ChartController(IChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping(value = "/chart/searchStockData")
    public String searchStockName() throws Exception {

        return "/chart/searchStockData";
    }

    /**
     * @param request 종목명, 시작날짜
     */
    @GetMapping(value = "/chart/insertStockData")
    public String insertStockData(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".insertStockData start");

        String msg = "데이터 입력 완료";

        StockDTO pDTO = new StockDTO();

        // name 을 code 로 변환
        String name = request.getParameter("name");
        pDTO.setName(name);
        try {
            pDTO.setCode((chartService.getStockCodeByName(pDTO)).getCode());
        } catch (NullPointerException e) {
            log.info("코드 가져오기 실패. 유효하지 않은 이름.");
            msg = "데이터 입력 실패";
        }
        log.info("변환된 종목 코드 : " + pDTO.getCode());

        if (pDTO.getCode() == null) {
            msg = "이름 잘못 입력";
            log.info("유효하지 않은 이름");
        } else {
            // 데이터 중복 입력 방지 위해 데이터 존재여부 검사
            pDTO.setEnd_date((chartService.getStockCodeByName(pDTO)).getEnd_date());//데이터 db insert 시 마지막날짜(=입력된 db의 시작날짜)

            String start_date = request.getParameter("start_date");
            pDTO.setStart_date(start_date);

            log.info("requested name : " + pDTO.getName());
            log.info("requested start_date : " + pDTO.getStart_date());
            log.info("selected code : " + pDTO.getCode());
            log.info("처리된 end_date : " + pDTO.getEnd_date());

            if (pDTO.getEnd_date() != null) {
                log.info("기존 데이터 존재. db 입력된 처음 날짜 : " + pDTO.getEnd_date());
                if (Integer.parseInt(pDTO.getStart_date()) < Integer.parseInt(pDTO.getEnd_date())) {
                    chartService.insertStockData(pDTO);
                } else {
                    msg = "데이터 입력 건너뜀";
                    log.info(msg);
                }
            } else {
                log.info("기존 데이터 없음. 입력 시작 날짜 : " + pDTO.getStart_date());
                chartService.insertStockData(pDTO);
            }
        }

        log.info(this.getClass().getName() + ".insertStockData end");

        model.addAttribute("msg", msg);

        return "returnMsg";
    }

    @GetMapping(value = "/chart/viewStockChart")
    public String viewStockChart() {

        return "chart/viewStockChart";
    }

    // 종목명 입력시 db에서 가져온후 차트그리기
    @GetMapping(value = "/chart/getStockData")
    public String getStockData(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".getStockData start");

        StockDTO pDTO = new StockDTO();

        String name = request.getParameter("name");
        pDTO.setName(name);

        List<StockDTO> rList = chartService.getStockData(pDTO);

        model.addAttribute("rList", rList);
        model.addAttribute("name", name);

        log.info(this.getClass().getName() + ".getStockData end");

        return "/chart/viewStockChart";
    }
}
