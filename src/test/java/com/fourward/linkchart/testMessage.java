package com.fourward.linkchart;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

public class testMessage {
    public static void main(String[] args) {
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("", "", "https://api.solapi.com");// api key, api secret
// Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
        Message message = new Message();
        message.setFrom("");// 등록된 전화번호
        message.setTo("");// 보내는 전화번호
        message.setText("[linkchart] 인증번호 : ******.");// 전송 메시지

        try {
            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
