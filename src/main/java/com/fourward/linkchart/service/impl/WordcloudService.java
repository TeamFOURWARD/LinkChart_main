package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.service.INewsService;
import kr.co.shineware.nlp.komoran.core.Komoran;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("WordcloudService")
public class WordcloudService {

    // NewService에서 처리된 내용을 가져오기 위해 선언
    @Resource(name="NewsService")
    private INewsService newsService;

    Komoran nlp = null;



}
