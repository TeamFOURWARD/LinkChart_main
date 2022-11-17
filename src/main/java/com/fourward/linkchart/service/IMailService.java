package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.MailDTO;

public interface IMailService {

    //메일 발송
    int doSendMail(MailDTO pDTO);
}
