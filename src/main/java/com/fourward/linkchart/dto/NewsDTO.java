package com.fourward.linkchart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fourward.linkchart.service.impl.NewsService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDTO {
    private String rank;
    private String link;
    private String head;
    private String summary;
    private String publisher;
    private String date;
    private String thumb;

    //NewsService 사용
    private String name;
    private String start_date;
    private String end_date;
}
