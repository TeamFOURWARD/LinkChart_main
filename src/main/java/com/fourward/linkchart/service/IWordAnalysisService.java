package com.fourward.linkchart.service;

import java.util.List;
import java.util.Map;
public interface IWordAnalysisService {

    /**
     * 자연어 처리 : 형태소 분석 ( 명사만 추출 )
     * @param : 분석할 문장
     * @return : 분석 결과
     * */
    List<String> doWordNouns(String text) throws Exception;

    /**
     * 빈도수 분석(단어별 출현 빈도수)
     * @param : 명사만 추출된 단어 모음(리스트)
     * @return : 분석 결과
     * */
    Map<String, Integer> doWordCount(List<String> pList) throws Exception;

    /**
     * 분석할 문장의 자연어 처리 및 빈도수 분석 수행
     * @param : 분석할문장
     * @return : 분석 결과
     * */
    Map<String, Integer> doWordAnalysis(String text) throws Exception;
}
