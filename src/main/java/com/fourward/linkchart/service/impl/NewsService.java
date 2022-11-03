package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.dto.NewsRelatedDTO;
import com.fourward.linkchart.service.INewsService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("NewsService")
public class NewsService implements INewsService {


    public String doNaverNewsContents(String url) throws Exception {

        log.info(getClass().getName() + "newscollect Start");

        Document doc = null;

        doc = Jsoup.connect(url).get();

        Elements newsContent = doc.select("div._article_body_contents");

        String res = newsContent.text();

        log.info(res);

        doc = null;

        return res;
    }

    @Override
    public List<Map<String, Object>> getNewsContents(NewsDTO rDTO) {
        log.info(this.getClass().getName() + ".getNewsContents Start");

        List<Map<String, Object>> newsGroup_list = new ArrayList<>();

        final String keyword = rDTO.getName();
        final String start_date = rDTO.getStart_date();
        final String end_date = rDTO.getEnd_date();
        final String connUrl = "https://search.naver.com/search.naver?where=news&query=" + keyword + "&pd=3&ds=" + start_date + "&de=" + end_date;
        final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

        //중복부분 유틸클래스로 변경 예정.
        try {
            Connection conn = Jsoup.connect(connUrl).header("Content-Type", "application/json;charset=UTF-8").userAgent(USER_AGENT).method(Connection.Method.GET).ignoreContentType(true);

            //get 가져오기부분
            Document doc = conn.get();

            //뉴스 박스 각각을 리스트로
            Elements newsGroup_elements = doc.select("div.group_news ul li.bx");
            int i = 1;
            for (Element newsGroup : newsGroup_elements) {
                log.info("\n@@@@@@@@@@@@@@@@@@@@@ 기사 목록 " + i + " @@@@@@@@@@@@@@@@@@@@@@@@@");

                //날짜
                String date = newsGroup.select("div.info_group span.info").text();
                log.info("@ 기사 날짜 : " + date);
                log.info("@ ---------------------------------------------");

                //헤드 텍스트 & 링크
                String head_text = newsGroup.selectFirst("a.news_tit").attr("title");
                String head_link = newsGroup.selectFirst("a.news_tit").attr("abs:href");
                log.info("@ 헤드_텍스트 : " + head_text);
                log.info("@ 헤드_링크 : " + head_link);
                log.info("@ ---------------------------------------------");

                //내용 요약
                String summary = newsGroup.selectFirst("div.dsc_wrap a").text();
                log.info("@ 본문 요약 : " + summary);
                log.info("@ ---------------------------------------------");

                //작성 언론사
                String publisher = newsGroup.select("a.info.press").text().replace("언론사 선정", "");
                log.info("@ 작성 언론사 : " + publisher);
                log.info("@ ---------------------------------------------");

                //뉴스그룹 저장
                NewsDTO pNewsDTO = new NewsDTO();
                pNewsDTO.setDate(date);
                pNewsDTO.setHead(head_text);
                pNewsDTO.setLink(head_link);
                pNewsDTO.setPublisher(publisher);
                pNewsDTO.setSummary(summary);
                pNewsDTO.setRank(Integer.toString(i++));

                //썸네일
                Element element_thumb = newsGroup.selectFirst("a.dsc_thumb img");
                if (element_thumb != null) {
                    String thumb = element_thumb.attr("abs:src");
                    log.info("@ 썸네일_링크 : " + thumb);
                    log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                    pNewsDTO.setThumb(thumb);
                }

                //연관 뉴스그룹 저장
                List<NewsRelatedDTO> newsGroup_related_list = new ArrayList<>();

                Elements newsRelated_elements = newsGroup.select("div.news_cluster ul.list_cluster li.sub_bx");
                int j = 1;
                for (Element newsRelated : newsRelated_elements) {
                    log.info("\n############## " + (i - 1) + " 기사의 연관 기사 목록 " + j + " ###############");
                    //연관기사 날짜
                    String date_related = newsRelated.select("span.sub_txt").text();
                    log.info("# 기사 날짜 : " + date_related);

                    //연관기사 헤드(링크)
                    String head_link_related = newsRelated.selectFirst("a.elss.sub_tit").attr("abs:href");
                    String head_text_related = newsRelated.selectFirst("a.elss.sub_tit").attr("title");
                    log.info("# 연관기사 헤드_텍스트 : " + head_text_related);
                    log.info("# 연관기사 헤드_링크 : " + head_link_related);

                    //연관기사 해당 언론사
                    String publisher_related = newsRelated.selectFirst("cite.sub_txt").attr("title");
                    log.info("# 작성 언론사 : " + publisher_related);

                    log.info("########################################");

                    NewsRelatedDTO pNewsRelatedDTO = new NewsRelatedDTO();
                    pNewsRelatedDTO.setDate_related(date_related);
                    pNewsRelatedDTO.setLink_related(head_link_related);
                    pNewsRelatedDTO.setHead_related(head_text_related);
                    pNewsRelatedDTO.setPublisher_related(publisher_related);
                    pNewsRelatedDTO.setRank_related(Integer.toString(j++));

                    newsGroup_related_list.add(pNewsRelatedDTO);
                }
                Map<String, Object> newsGroup_map = new HashMap<>();
                newsGroup_map.put("news", pNewsDTO);
                newsGroup_map.put("newsGroup_related_list", newsGroup_related_list);
                newsGroup_list.add(newsGroup_map);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".getNewsContents End");

        return newsGroup_list;
    }
}
