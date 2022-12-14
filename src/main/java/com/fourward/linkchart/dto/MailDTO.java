package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {
    private String from;
    private String to;
    private String subject;
    private String text;
}
