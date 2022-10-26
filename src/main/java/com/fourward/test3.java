package com.fourward;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class test3 {
    public static void main(String[] args) {

        final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

        try {
            String connUrl = "https://search.naver.com/search.naver?where=news&query=애플&pd=3&ds=20211023&de=20211025";

            Connection conn = Jsoup.connect(connUrl)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .userAgent(USER_AGENT)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true);

            //get 가져오기부분
            Document doc = conn.get();
            //뉴스 박스 각각을 리스트로
            Elements newsGroup_elements = doc.select("div.group_news ul li.bx");

            int i = 1;
            for (Element newsGroup : newsGroup_elements) {
                System.out.println("\n@@@@@@@@@@@@@@@@@@@@@ 기사 목록 "+i+++" @@@@@@@@@@@@@@@@@@@@@@@@@");

                //날짜
                String date = newsGroup.selectFirst("div.info_group span.info").text();
                System.out.println("@ 기사 날짜 : " + date);
                System.out.println("@ ---------------------------------------------");

                //헤드 텍스트 & 링크
                String head_text = newsGroup.selectFirst("a.news_tit").attr("title");
                String head_link = newsGroup.selectFirst("a.news_tit").attr("abs:href");
                System.out.println("@ 헤드_텍스트 : " + head_text);
                System.out.println("@ 헤드_링크 : " + head_link);
                System.out.println("@ ---------------------------------------------");

                //내용 요약
                String summary = newsGroup.selectFirst("div.dsc_wrap a").text();
                System.out.println("@ 본문 요약 : " + summary);
                System.out.println("@ ---------------------------------------------");

                //작성 언론사
                String publisher = newsGroup.select("a.info.press").text().replace("언론사 선정", "");
                System.out.println("@ 작성 언론사 : " + publisher);
                System.out.println("@ ---------------------------------------------");

                //썸네일
                Element element_thumb = newsGroup.selectFirst("a.dsc_thumb img");
                if (element_thumb != null) {
                    String thumb = element_thumb.attr("abs:src");
                    System.out.println("@ 썸네일_링크 : " + thumb);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }

                Elements newsRelated_elements = newsGroup.select("div.news_cluster ul.list_cluster li.sub_bx");

                int j = 1;
                for (Element newsRelated : newsRelated_elements) {
                    System.out.println("\n############## "+(i-1)+" 기사의 연관 기사 목록 "+j+++" ###############");
                    //연관기사 날짜
                    String date_related = newsRelated.selectFirst("span.sub_txt").text();
                    System.out.println("# 기사 날짜 : " + date_related);

                    //연관기사 헤드(링크)
                    String head_link_related = newsRelated.selectFirst("a.elss.sub_tit").attr("abs:href");
                    String head_text_related = newsRelated.selectFirst("a.elss.sub_tit").attr("title");
                    System.out.println("# 연관기사 헤드_텍스트 : " + head_text_related);
                    System.out.println("# 연관기사 헤드_링크 : " + head_link_related);

                    //연관기사 해당 언론사
                    String publisher_related = newsRelated.selectFirst("cite.sub_txt").attr("title");
                    System.out.println("# 작성 언론사 : " + publisher_related);

                    System.out.println("########################################");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
