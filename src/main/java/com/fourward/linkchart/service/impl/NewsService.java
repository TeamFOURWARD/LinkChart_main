package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.NewsDTO;
import com.fourward.linkchart.dto.NewsRelatedDTO;
import com.fourward.linkchart.dto.NewsReqDTO;
import com.fourward.linkchart.service.INewsService;
import com.fourward.linkchart.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Calendar.DATE;

@Slf4j
@Service("NewsService")
public class NewsService implements INewsService {
    @Override
    public List<Map<String, Object>> getNewsContents(NewsReqDTO rDTO) throws Exception {
        log.info("{}.getNewsContents start", this.getClass().getName());
        // 날짜 null 일때 기본값 오늘
        if (rDTO.getDate().equals("")) {
            rDTO.setDate(DateUtil.getNowDate());
        }
        rDTO.setDate_begin(DateUtil.changeDate(rDTO.getDate(), DATE, -5));

        StringBuilder url = new StringBuilder("https://search.naver.com/search.naver?where=news&pd=3");
        url.append("&query=");
        url.append(rDTO.getKeyword());
        url.append("&ds=");
        url.append(rDTO.getDate_begin());
        url.append("&de=");
        url.append(rDTO.getDate());
        log.info("get news url ~~~~~~~~ : [{}]", url);
        Document doc = (
                Jsoup.connect(url.toString()).header("Content-Type", "application/json;charset=UTF-8")
                        .userAgent(USER_AGENT).method(Connection.Method.GET)
                        .ignoreContentType(true)
        ).get();

        int i = 1, j = 1;//수집된 기사 개수
        List<Map<String, Object>> newsGroup_list = new ArrayList<>();
        {//뉴스 박스 각각을 리스트로
            Elements newsGroup_elements = doc.select("div.group_news ul li.bx");
            for (Element newsGroup : newsGroup_elements) {
                NewsDTO pNewsDTO = new NewsDTO();
                pNewsDTO.setDate(newsGroup.select("div.info_group span.info").text());
                pNewsDTO.setHead(newsGroup.selectFirst("a.news_tit").attr("title"));
                pNewsDTO.setLink(newsGroup.selectFirst("a.news_tit").attr("abs:href"));
                pNewsDTO.setPublisher(newsGroup.select("a.info.press").text().replace("언론사 선정", ""));
                pNewsDTO.setSummary(newsGroup.selectFirst("div.dsc_wrap a").text());
                pNewsDTO.setRank(Integer.toString(i++));

                Element element_thumb = newsGroup.selectFirst("a.dsc_thumb img");
                if (element_thumb != null) {
                    pNewsDTO.setThumb(element_thumb.attr("abs:src"));
                }
                //연관 뉴스그룹 저장
                List<NewsRelatedDTO> newsGroup_related_list = new ArrayList<>();
                Elements newsRelated_elements = newsGroup.select("div.news_cluster ul.list_cluster li.sub_bx");
                for (Element newsRelated : newsRelated_elements) {
                    NewsRelatedDTO pNewsRelatedDTO = new NewsRelatedDTO();
                    pNewsRelatedDTO.setDate_related(newsRelated.select("span.sub_txt").text());
                    pNewsRelatedDTO.setLink_related(newsRelated.selectFirst("a.elss.sub_tit").attr("abs:href"));
                    pNewsRelatedDTO.setHead_related(newsRelated.selectFirst("a.elss.sub_tit").attr("title"));
                    pNewsRelatedDTO.setPublisher_related(newsRelated.selectFirst("cite.sub_txt").attr("title"));
                    pNewsRelatedDTO.setRank_related(Integer.toString(j++));

                    newsGroup_related_list.add(pNewsRelatedDTO);
                }
                Map<String, Object> newsGroup_map = new HashMap<>();
                newsGroup_map.put("news", pNewsDTO);
                newsGroup_map.put("newsGroup_related_list", newsGroup_related_list);
                newsGroup_list.add(newsGroup_map);

            }
        }
        log.info("number of news collected : [{}]", i);
        log.info("number of related news collected : [{}]", j);

        log.info("{}.getNewsContents end", this.getClass().getName());

        return newsGroup_list;
    }

//    public Map<String, Integer> getResult(String response, String[] fileds) {
//
//        Map<String, Object> rtnObj = new HashMap<>();
//
//        log.info(this.getClass().getName() + ".getResult Start");
//        try {
//            JSONParser parser = new JSONParser();
//            JSONObject result = (JSONObject) parser.parse(response);
//            rtnObj.put("news", (long) result.get("news"));
//        }
//        NewsReqDTO newsReqDTO = new NewsReqDTO();
//
////        String newContext = this.doWordAnalysis(newsReqDTO);
////
////        List<String> rList = this.doWordNouns(newContext)
//
//        return null;
//    }
}