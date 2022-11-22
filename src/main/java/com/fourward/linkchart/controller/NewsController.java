package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.service.INewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/news")
public class NewsController {
    private final INewsService newsService;

    @GetMapping(value = "/getNewsData")
    public List<Map<String, Object>> getNewsContents(HttpServletRequest request) {
        log.info("{}.getNewsData start", this.getClass().getName());

        NewsDTO pDTO = new NewsDTO();
        pDTO.setName(request.getParameter("keyword"));
        pDTO.setEnd_date(request.getParameter("date"));
        log.info("requested keyword : [{}]", pDTO.getName());
        log.info("requested date : [{}]", pDTO.getDate());

        List<Map<String, Object>> rNewsList;
        try {
            rNewsList = newsService.getNewsContents(pDTO);
        } catch (Exception ignored) {

            return null;
        }
        log.info("{}.getNewsData end", this.getClass().getName());

        return rNewsList;
    }
}