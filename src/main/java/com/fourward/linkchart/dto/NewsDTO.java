package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDTO {
    private String news_date;  // 날짜
    private String seq;        // 순번
    private String stock_name; // 종목 이름
}
