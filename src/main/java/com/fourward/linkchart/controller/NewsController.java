package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.service.INewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/news")
public class NewsController {
    private final INewsService newsService;

    @GetMapping(value = "/getNewsData")
    public List<Map<String, Object>> getNewsContents(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".getNewsData start");

        final String keyword = request.getParameter("keyword");
        final String end_date = request.getParameter("date");
        log.info("requested keyword : " + keyword);
        log.info("requested date : " + end_date);

        NewsDTO pDTO = new NewsDTO();
        pDTO.setName(keyword);
        pDTO.setEnd_date(end_date);

        List<Map<String, Object>> rNewsList = new ArrayList<>();
        try {
            rNewsList = newsService.getNewsContents(pDTO);
        } catch (NullPointerException e) {
            log.info(this.getClass().getName() + ".getNewsData failed");
        } finally {
            log.info(this.getClass().getName() + ".getNewsData end");

        }

        return rNewsList;
    }
}
