package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {
    private String toMail; // 받는 사람
    private String title; // 보내는 메일 제목
    private String contents; // 보내는 메일 내용
}
