package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.service.INewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("WordAnalysisService")
public class WordAnalysisService implement IWordAnalysisService {

    @Resource(name = "NewsService")
    private INewsService newsService;

    Komoran nlp = null;
}
