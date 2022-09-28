package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.persistence.mapper.INewsMapper;
import com.fourward.linkchart.service.INewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service("NewsService")
public class NewsService implements INewsService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성
    // moiveMapper 변수에 이미 메모리에 올라간 Mapper 객체를 넣음
    // autowired 어노테이션 대신 사용
    private final INewsMapper newsMapper;
    /**
    * JSOUP 라이브러리를 사용해 뉴스정보 가져오기
     */
    @Transactional
    @Override
    public int collectNewsInfo() throws Exception {
        log.info(this.getClass().getName() + ".collectNewsInfo Start");

        int res = 0; // 0보다 크면 크롤링 성공

        // 사이트 주소 입력 , url변수에 주소를 담음
        // 현재 url 삼성전자로 설정 된 상태
        // 종목명을 변경하여 반복해 가져와야 하기 때문에 수정 예정
        String url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=news&query=%EC%82%BC%EC%84%B1%EC%A0%84%EC%9E%90&nso=so%3Ar%2Cp%3Afrom20200101to20200103&de=2020.01.03&ds=2020.01.01&mynews=0&office_section_code=0&office_type=0&pd=3&photo=3&sort=0";

        // JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML소스 저장할 변수
        // Document 를 사용
        Document doc = null;

        //사이트 접속(http 프로토콜만 가능, https 프로토콜은 보안때문에 안됨)
        doc = Jsoup.connect(url).get(); // url 값을 get()얻어서 doc변수에 대입

        // 웹페이지 태그 선택
        // 작성된 cssQuery의 HTML 소스만 element 변수에 저장
        Elements element = doc.select("div.news_info");

        Iterator<Element> news_date = element.select("").iterator(); // 뉴스 날짜

        NewsDTO pDTO = null;

        //수집된 데이터 DB저장
        while(news_date.hasNext()) {

            pDTO = new NewsDTO();
        }
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
