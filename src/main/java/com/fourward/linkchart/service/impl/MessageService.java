package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.MessageDTO;
import com.fourward.linkchart.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService implements IMessageService {
    @Value("${message.api.key}")
    private String key;
    @Value("${message.api.secret}")
    private String secret;

    @Value("${message.api.from.personal}")
    private String from_;

    @Override
    public void sendMessage(MessageDTO messageDTO) throws Exception {
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(key, secret, "https://api.solapi.com");// api key, api secret
        // Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
        Message message = new Message();
        message.setFrom(from_);// 등록된 전화번호
        message.setTo(messageDTO.getTo());// 보내는 전화번호
        message.setText(messageDTO.getText());// 전송 메시지

        messageService.send(message);// send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
    }
}