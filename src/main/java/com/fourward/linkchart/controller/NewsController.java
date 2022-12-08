package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.ImageDTO;
import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.service.INewsService;
import com.fourward.linkchart.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/news")
public class NewsController {
    private final INewsService newsService;
    private final ImageService imageService;

    @GetMapping(value = "/getNewsData")
    public Map<String ,Object> getNewsContents(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".getNewsData start");

        final String keyword = request.getParameter("keyword");
        final String end_date = request.getParameter("date");
        log.info("requested keyword : {}", keyword);
        log.info("requested date : {}", end_date);

        NewsDTO pDTO = new NewsDTO();
        pDTO.setName(keyword);
        pDTO.setEnd_date(end_date);

        List<Map<String, Object>> rNewsList = newsService.getNewsContents(pDTO);


        log.info(this.getClass().getName() + ".getNewsData end");
        ImageDTO imageDTO = imageService.getImageByImageName(keyword);

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", rNewsList);
        map.put("image", imageDTO);

        return map;
    }
}