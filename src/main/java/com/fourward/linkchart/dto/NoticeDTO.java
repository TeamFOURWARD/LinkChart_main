package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterestsDTO {
    private String stock_code_number;//주식 코드 번호
    private String stock_name; //주식 이름
    private String stock_date; // 날짜
    private String stock_price; //주식 가격
    private String like_total; // 좋아요 총합
}
