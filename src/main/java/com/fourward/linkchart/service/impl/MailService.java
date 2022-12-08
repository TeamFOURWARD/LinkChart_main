package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.MailDTO;
import com.fourward.linkchart.service.IMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service("MailService")
@RequiredArgsConstructor
public class MailService implements IMailService {
    private final JavaMailSender javaMailSender;

    @Override
    public int doSendMail(MailDTO mailDTO) {
        log.info("{}.doSendMail start", this.getClass().getName());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailDTO.getTo());
        simpleMailMessage.setSubject(mailDTO.getSubject());
        simpleMailMessage.setText(mailDTO.getText());
        simpleMailMessage.setFrom(mailDTO.getFrom());
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.info(e.getMessage());
            return 1;
        }
        log.info("{}.doSendMail end", this.getClass().getName());
        //메일 발송 성공여부(발송성공 : 1 / 발송실패 : 0)
        return 0;
    }
}