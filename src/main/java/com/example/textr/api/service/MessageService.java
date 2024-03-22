package com.example.textr.api.service;

import com.example.textr.exception.CustomisedException;
import com.example.textr.records.MessageRecord;
import com.example.textr.records.UserTextHistoryRecord;

import java.util.List;

public interface MessageService {

    MessageRecord sendMessage(String message, Long id) throws CustomisedException;


    List<MessageRecord> getMessages(Long from, Long to);

    Long getNewMessagesCount(Long from, Long to);

    List<UserTextHistoryRecord> getAllUsersTextHistory(Long from);
}
