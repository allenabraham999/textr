package com.example.textr.utils.mappers;

import com.example.textr.entity.Message;
import com.example.textr.records.MessageRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageMapper {
    public static List<MessageRecord> messageToMessageRecord(List<Message> messages){
        List<MessageRecord> messagesList = new ArrayList<>();
        for(Message message: messages){
            messagesList.add(MessageRecord.builder()
                            .message(message.getMessage())
                            .sendTo(message.getReceiverId().getId())
                    .build());
        }
        return messagesList;
    }
    public static MessageRecord messageToMessageRecord(Message message){
        return MessageRecord.builder()
                .sendTo(message.getReceiverId().getId())
                .message(message.getMessage())
                .build();
    }
}
