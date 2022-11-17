package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.NewsDTO;

import java.util.List;
import java.util.Map;

public interface INewsService {
    List<Map<String, Object>> getNewsContents(NewsDTO rDTO);
}
