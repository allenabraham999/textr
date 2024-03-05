package com.example.textr.api.service;

import com.example.textr.dto.BaseDto;
import com.example.textr.entity.Message;
import com.example.textr.records.MessageRecord;

import java.util.List;

public interface MessageService {

    MessageRecord sendMessage(String message, Long id);


    List<MessageRecord> getMessages(Long from, Long to);
}
