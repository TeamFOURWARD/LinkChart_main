package com.fourward.linkchart.controller;

import com.fourward.linkchart.service.INewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller("NewsController")
public class NewsController implements INewsService {

    @GetMapping(value = "/chart/viewStockChart")
    public String indexPage() throws Exception {
        log.info(this.getClass().getName() + "News page Start");
        log.info(this.getClass().getName() + "News page End");

        return "viewStockChart"; // chart와 뉴스데이터를 한페이지에 표현할 계획
    }
}
