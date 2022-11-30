package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.NewsReqDTO;

import java.util.List;
import java.util.Map;

public interface INewsService {
    String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    List<Map<String, Object>> getNewsContents(NewsReqDTO rDTO) throws Exception;
}