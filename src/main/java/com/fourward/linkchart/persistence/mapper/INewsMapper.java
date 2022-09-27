package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.NewsDTO;

public interface INewsMapper {

    int InsertNewsInfo(NewsDTO pDTO) throws Exception;
}
