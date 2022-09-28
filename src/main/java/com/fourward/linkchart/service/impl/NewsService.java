package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.persistence.mapper.INewsMapper;
import com.fourward.linkchart.service.INewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RequiredArgsConstructor // 생성자를 자동으로 생성함
@Service("NewsService")
public class NewsService implements INewsService {

    // newsMapper 변수에 이미 메모리에 올라간 Mapper 객체를 넣어줌
    private final INewsMapper newsMapper;

    /**
     * JSOUP 라이브러리를 활용한 네이버 뉴스 정보 가져오기
     */
    @Transactional
    // 모든 작업들을 원상태로 되돌릴 수 있음
    // 수집할 영화 정보를 DB에 저장하기 때문에 반드시 작성
    @Override
    public int collectNewsInfo() throws Exception {
        //로그 찍기
        log.info(this.getClass().getName() + ".collectNewInfo Start");
        int res = 0; // 크롤링 결과 (0보다 크면 크롤릴 성공)

        String url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=news&query=삼성전자&nso=so%3Ar%2Cp%3Afrom20200101to20200103&de=2020.01.03&ds=2020.01.01&mynews=0&office_section_code=0&office_type=0&pd=3&photo=3&sort=0";
        Document doc = null;

        doc = Jsoup.connect(url).get();

        Elements element = doc.select("div.group_news");
        log.info(this.getClass().getName() + ".collectNewInfo End");


        return res;
    }

    /**
     * @param rList 뉴스 정보 별 각각의 DTO 를 담은 List
     * @return 뉴스 정보를 담은 리스트
     */
    @Override
    public List<NewsDTO> getNewsContents(List<NewsDTO> rList) throws Exception {
        log.info(this.getClass().getName() + ".getNewsContents Start!");
        log.info(this.getClass().getName() + ".getNewsContents End!");
        //https://search.naver.com/search.naver?sm=tab_hty.top&where=news&query=애플&nso=so%3Ar%2Cp%3Afrom20200101to20200103&de=2020.01.03&ds=2020.01.01&mynews=0&office_section_code=0&office_type=0&pd=3&photo=3&sort=0
        return rList;
    }


}