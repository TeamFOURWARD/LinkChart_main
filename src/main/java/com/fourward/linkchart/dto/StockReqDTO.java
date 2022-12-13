package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockReqDTO {
    private String name;
    private String startTime;
    private String endTime;
    private String timeframe;
    private String symbol;
    private String startTime_exist;
    private String endTime_exist;
}
