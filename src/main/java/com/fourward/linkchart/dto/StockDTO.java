package com.fourward.linkchart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockDTO {
    private String code;
    private String name;
    private String date;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;

    private String startDate_exist;
    private String endDate_exist;
    private String startDate_req;
    private String endDate_req;
}
