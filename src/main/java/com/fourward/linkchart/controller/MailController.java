package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.MailDTO;
import com.fourward.linkchart.service.IMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user/mail")
public class MailController {
    private final IMailService mailService;

    @PostMapping(value = "/send")
    public String sendMail(MailDTO mailDTO) {
        log.info("{}.sendMail start", this.getClass().getName());

        String toMail = mailDTO.getTo();
        //String title = mailDTO.getTitle();
//        String content = mailDTO.getContent();


        int res = mailService.doSendMail(mailDTO);

        if (res == 1) {
            log.info(this.getClass().getName() + "mail.sendMail success!!!");

        } else { //메일발송 실패
            log.info(this.getClass().getName() + "mail.sendMail fail!!!");

        }


        log.info("{}.sendMail end", this.getClass().getName());

        return "/mail/sendMailResult";
    }
}