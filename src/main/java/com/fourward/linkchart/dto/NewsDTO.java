package com.fourward.linkchart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsDTO {
    private String rank;
    private String link;
    private String head;
    private String summary;
    private String publisher;
    private String date;
    private String thumb;
}