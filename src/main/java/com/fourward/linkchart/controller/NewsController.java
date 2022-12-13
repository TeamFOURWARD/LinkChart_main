package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.dto.NewsReqDTO;
import com.fourward.linkchart.service.INewsService;
import com.fourward.linkchart.service.IWordAnalysisService;
import com.fourward.linkchart.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final IWordAnalysisService wordAnalysisService;

    @GetMapping(value = "/getNewsData")
    public ResponseEntity<Map<String, Object>> getNewsContents(@ModelAttribute NewsReqDTO pDTO) {
        log.info("{}.getNewsData start", this.getClass().getName());
        log.info("requested keyword : [{}]", pDTO.getKeyword());
        log.info("requested date : [{}]", pDTO.getDate());

        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> rNewsList;
        StringBuilder sb = new StringBuilder();
        try {
            rNewsList = newsService.getNewsContents(pDTO);
            //wordAnalysisService.doWordAnalysis();
            rNewsList.forEach(l ->
                    sb.append(((NewsDTO) l.get("news")).getSummary())
            );
        } catch (Exception ignored) {
            // 서버 에러 (TODO 헤더 첨가)
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // newsRes 워드클라우드에 사용할 뉴스데이터가 담긴 변수
        String newsRes = sb.toString();
        try {
            map.put("wordcloud", wordAnalysisService.doWordAnalysis(newsRes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("newsList", rNewsList);
        map.put("image", imageService.getImageByImageName(pDTO.getKeyword()));
        log.info("{}.getNewsData end", this.getClass().getName());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}