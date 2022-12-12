package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.WordcloudDTO;
import com.fourward.linkchart.service.IWordAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;
@RestController // controller내 함수들의 결과를 json으로 받음
@Slf4j
public class WordcloudController {

    @Resource(name = "WordAnalysisService")
    private IWordAnalysisService wordAnalysisService;
/*
    @PostMapping(value = "/wordcloud")
    public Map<String, Integer> analysis(@RequestBody WordcloudDTO wordcloudDTO) throws Exception {
        log.info(this.getClass().getName() + ".analysis Start");

        //워드클라우드로 만들 뉴스 기사 데이터 변수
        String text = "뉴스 기사";

        Map<String, Integer> rMap = wordAnalysisService.doWordAnalysis(text);

        if (rMap == null) {
            rMap = new HashMap<String, Integer>();
        }
        return rMap;
    }*/
}
