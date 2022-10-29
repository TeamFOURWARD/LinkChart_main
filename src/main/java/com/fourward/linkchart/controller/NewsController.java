package com.fourward.linkchart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.dto.NewsRelatedDTO;
import com.fourward.linkchart.service.INewsService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "news")
public class NewsController {

    private final INewsService newsService;

    public NewsController(INewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping(value = "/getNewsData")
    public String getNewsContents(HttpServletRequest request, Model model) {
        log.info(this.getClass().getName() + ".getNewsData Start");

        final String keyword = request.getParameter("name");
        final String end_date = request.getParameter("date");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        try {
            Date date1 = sdf.parse(end_date);
            c1.setTime(date1);
            c1.add(Calendar.DATE, -3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String start_date = sdf.format(c1.getTime());

        NewsDTO pDTO = new NewsDTO();
        pDTO.setName(keyword);
        pDTO.setStart_date(start_date);
        pDTO.setEnd_date(end_date);

        List<Map<String, Object>> newsGroup_list = newsService.getNewsContents(pDTO);

           /*
           ObjectMapper objectMapper = new ObjectMapper();
           model.addAttribute("newsData", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newsGroup_list));
           */

        model.addAttribute("newsData", newsGroup_list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("date", end_date);

        log.info(this.getClass().getName() + ".getNewsData End");

        return "/news/viewNewsContents";
    }
}
