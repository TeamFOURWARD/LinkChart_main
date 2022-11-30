package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.NewsReqDTO;
import com.fourward.linkchart.service.INewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/news")
public class NewsController {
    private final INewsService newsService;

    @GetMapping(value = "/getNewsData")
    public ResponseEntity<List<Map<String, Object>>> getNewsContents(@RequestParam String date, @RequestParam String keyword) {
        log.info("{}.getNewsData start", this.getClass().getName());
        NewsReqDTO pDTO = new NewsReqDTO();
        pDTO.setKeyword(keyword);
        pDTO.setDate(date);
        log.info("requested keyword : [{}]", keyword);
        log.info("requested date : [{}]", date);

        List<Map<String, Object>> rNewsList;
        try {
            rNewsList = newsService.getNewsContents(pDTO);
        } catch (Exception ignored) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("{}.getNewsData end", this.getClass().getName());

        return new ResponseEntity<>(rNewsList, HttpStatus.OK);
    }
}