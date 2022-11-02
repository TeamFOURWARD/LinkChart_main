package com.fourward.linkchart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsRelatedDTO {
    private String rank_related;
    private String link_related;
    private String head_related;
    private String publisher_related;
    private String date_related;
}
