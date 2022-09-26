package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.service.INewsService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


@Slf4j
@Service("NewsService")
public class NewsService implements INewsService {

    @Override
    public String doNaverNewsContents(String url) throws Exception {

        log.info(getClass().getName() + "newscollect start");

        Document doc = null;

        doc = Jsoup.connect(url).get();

        Elements newsContent = doc.select("div._article_body_contents");

        String res = newsContent.text();

        log.info(res);

        doc = null;

        return res;
    }
}
