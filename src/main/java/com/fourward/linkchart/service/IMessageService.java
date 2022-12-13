package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.MessageDTO;

public interface IMessageService {
    void sendMessage(MessageDTO messageDTO) throws Exception;
}