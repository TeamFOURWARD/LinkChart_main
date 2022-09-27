package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.service.INewsService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service("NewsService")
public class NewsService implements INewsService {


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

    /**
     *
     * @param rList 뉴스 정보 별 각각의 DTO 를 담은 List
     * @return 뉴스 정보를 담은 리스트
     */
    @Override
    public List<NewsDTO> getNewsContents(List<NewsDTO> rList) throws Exception {

        return null;
    }

}
