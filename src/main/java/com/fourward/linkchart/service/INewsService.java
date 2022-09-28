package com.fourward.linkchart.service;


import com.fourward.linkchart.dto.NewsDTO;

import java.util.List;

public interface INewsService {

    int collectNewsInfo() throws Exception;
    List<NewsDTO> getNewsContents(List<NewsDTO> rList) throws Exception;
}
