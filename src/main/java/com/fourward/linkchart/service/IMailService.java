package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.MailDTO;

public interface IMailService {
    int doSendMail(MailDTO mailDTO);
}