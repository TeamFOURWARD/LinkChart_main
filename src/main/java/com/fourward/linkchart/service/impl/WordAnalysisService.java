package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.service.INewsService;
import com.fourward.linkchart.service.IWordAnalysisService;
import com.fourward.linkchart.util.CmmUtil;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("WordAnalysisService")
public class WordAnalysisService implements IWordAnalysisService {

    private INewsService NewsService;

    // 추후 메모리에 올리기 위해 전역 변수로 선언
    Komoran nlp = null;

    public WordAnalysisService(){
        log.info(this.getClass().getName() + ".WordAnalysisService Start");


        this.nlp = new Komoran(DEFAULT_MODEL.LIGHT);

        log.info("KOMORAN(형태소분석기) nlp변수에 객체 생성 완료");
        log.info(this.getClass().getName() + ".wordAnalysisService Creater End");

    }
    @Override
    public List<String> doWordNouns(String text) throws Exception {

        //String newstext = newsRes;
        log.info(this.getClass().getName() + ".doWordAnalysis Start!");

        // 불용어 제거
        String replace_text = text.replaceAll("[^가-힣a-zA-Z0-9]"," ");
        log.info("StopWord start");

        // 공백 제거
        String trim_text = replace_text.trim();
        log.info("trim() start");

        // 형태소분석 시작
        KomoranResult analyzeResultList = this.nlp.analyze(trim_text);

        // 명사 추출 getNouns()
        List<String> rList = analyzeResultList.getNouns();

        if (rList == null) {
            rList = new ArrayList<String>();
        }

        // 분석 결과 확인
        Iterator<String> it = rList.iterator();

        while (it.hasNext()) {

            //추출 명사
            String word = CmmUtil.nvl(it.next());
            log.info("추출 명사 : " + word);
        }
        log.info(this.getClass().getName() + ".doWordAnalysis End!");

        return rList;
    }

    @Override
    public Map<String, Integer> doWordCount(List<String> pList) throws Exception {
        //중복 제거의 목적

        log.info(this.getClass().getName() + ".doWordCount Start");

        if(pList == null) {
            pList = new ArrayList<String>();
        }

        // 단어 빈도수 저장
        Map<String, Integer> rMap = new HashMap<>();

        // 중복 제거를 위해 Set 데이터타입에 저장함
        // rSet에 중복 되지 않은 단어 저장
        Set<String> rSet = new HashSet<String>(pList);

        // 중복 제거 단어 빈도수 구하기
        Iterator<String> it = rSet.iterator();

        while (it.hasNext()) {

            String word = CmmUtil.nvl(it.next());

            int frequency = Collections.frequency(pList, word);

            log.info("word : " + word);
            log.info("frequency : " + frequency);

            // 단어 , 빈도수
            rMap.put(word, frequency);
        }
        log.info(getClass().getName() + ".doWordCount End!");
        return rMap;
    }

    @Override
    public Map<String, Integer> doWordAnalysis(String text) throws Exception {

        String newContext = "";
        // 명사 추출
        List<String> rList = this.doWordNouns(text);

        if (rList == null) {
            rList = new ArrayList<String>();
        }

        Map<String, Integer> rMap = this.doWordCount(rList);

        if (rMap == null){
            rMap = new HashMap<String, Integer>();

        }
        return rMap;
    }
}
