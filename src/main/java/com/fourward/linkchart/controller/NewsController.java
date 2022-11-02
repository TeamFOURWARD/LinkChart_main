package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.service.INewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "news")
public class NewsController {

    private final INewsService newsService;

    public NewsController(INewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping(value = "/getNewsData")
    @ResponseBody
    public List<Map<String, Object>> getNewsContents(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".getNewsData Start");

        final String keyword = request.getParameter("keyword");
        final String end_date = request.getParameter("date");
        log.info("requested keyword : " + keyword);
        log.info("requested date : " + end_date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        try {
            Date date1 = sdf.parse(end_date);
            c1.setTime(date1);
            c1.add(Calendar.DATE, -5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String start_date = sdf.format(c1.getTime());

        NewsDTO pDTO = new NewsDTO();
        pDTO.setName(keyword);
        pDTO.setStart_date(start_date);
        pDTO.setEnd_date(end_date);

        List<Map<String, Object>> newsGroup_list = newsService.getNewsContents(pDTO);

        log.info(this.getClass().getName() + ".getNewsData End");

        return newsGroup_list;
    }
}
