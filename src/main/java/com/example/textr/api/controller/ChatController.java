package com.example.textr.api.controller;

import com.example.textr.annotation.APIResult;
import com.example.textr.api.service.MessageService;
import com.example.textr.entity.Message;
import com.example.textr.entity.User;
import com.example.textr.enums.MessageStatus;
import com.example.textr.exception.CustomisedException;
import com.example.textr.records.MessageNotification;
import com.example.textr.records.MessageRecord;
import com.example.textr.repository.MessageRepository;
import com.example.textr.repository.UserRepository;
import com.example.textr.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@RestController
@CrossOrigin(origins = "*")
public class ChatController {
    @Autowired
    MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;




//    @MessageMapping("/messages")
//    @SendTo("/topic/messages")
//    public List<MessageRecord> getMessagesSocket(Long from, Long to){
//        List<MessageRecord> messages = messageService.getMessages(from, to);
//
//        // Send messages to the specified destination
//        messagingTemplate.convertAndSend("/topic/messages", messages);
//        return messages;
//    }

    @MessageMapping("/message")
    public void sendMessage(@Payload MessageRecord messageRecord) throws CustomisedException {
        User from = userRepository.getUserById(messageRecord.from());
        User toSendTo = userRepository.getUserById(messageRecord.sendTo());
        if(toSendTo == null){
            throw new CustomisedException("User doesn't exist!");
        }
//        User user = Utils.getCurrentUser();
        Message message = Message.builder()
                .senderId(from)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .receiverId(userRepository.getUserById(messageRecord.sendTo()))
                .message(messageRecord.message())
                .status(MessageStatus.SENT)
                .build();
        Message savedMessage = messageRepository.save(message);
//Old one
//        messagingTemplate.convertAndSendToUser(String.valueOf(savedMessage.getReceiverId()),
//                "/queue/messages",
//                MessageNotification.builder()
//                        .receiverId(savedMessage.getReceiverId().getId())
//                        .senderId(savedMessage.getSenderId().getId())
//                        .build());
        String conversationQueue = "/queue/conversation/" + from.getId() + "/" + toSendTo.getId();
        messagingTemplate.convertAndSend(conversationQueue, MessageNotification.builder()
                .receiverId(savedMessage.getReceiverId().getId())
                .senderId(savedMessage.getSenderId().getId())
                .build());

    }
}
