package com.fourward;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class test {
    public static void main(String[] args) {
        httpClientMethod();
        jsoupMethod();
    }

    private static void jsoupMethod() {
        final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
        try {
            // 1. URL 선언
            String connUrl = "https://api.finance.naver.com/siseJson.naver?symbol=000020&requestType=1&startTime=20200409&endTime=20220917&timeframe=day";

            // 2. HTML 가져오기
            Connection conn = Jsoup.connect(connUrl)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .userAgent(USER_AGENT)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true);
            Document doc = conn.get();

            // 3. 가져온 HTML Document 를 확인하기
            System.out.println(doc.toString());

        } catch (IOException e) {
            // Exp : Connection Fail
            e.printStackTrace();
        }
    }


    private static void httpClientMethod() {
        final String USER_AGENT = "Mozila/5.0";
        final String GET_URL = "https://api.finance.naver.com/siseJson.naver?symbol=000020&requestType=1&startTime=20200409&endTime=20220917&timeframe=day";

        try {

            //http client 생성
            CloseableHttpClient httpClient = HttpClients.createDefault();

            //get 메서드와 URL 설정
            HttpGet httpGet = new HttpGet(GET_URL);

            //agent 정보 설정
            httpGet.addHeader("User-Agent", USER_AGENT);
            httpGet.addHeader("Content-type", "application/json");

            //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            System.out.println("GET Response Status");
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            System.out.println(json);

            httpClient.close();
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
    }
}
