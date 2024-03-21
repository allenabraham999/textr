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
import com.example.textr.security.utils.JwtTokenUtils;
import com.example.textr.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${spring.data.rest.base.path}"+"/secure/message")
@CrossOrigin(origins = "*")
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST)
    @APIResult(message = " ",error_message = " ", message_code = 0)
    public Object sendMessageRest(@RequestBody MessageRecord message) throws CustomisedException {
        return messageService.sendMessage(message.message(), message.sendTo());
    }



//    @MessageMapping("/messages")
//    @SendTo("/topic/messages")
//    public List<MessageRecord> getMessagesSocket(Long from, Long to){
//        List<MessageRecord> messages = messageService.getMessages(from, to);
//
//        // Send messages to the specified destination
//        messagingTemplate.convertAndSend("/topic/messages", messages);
//        return messages;
//    }

//    @MessageMapping("/messages")
//    public void sendMessage(@Payload MessageRecord messageRecord) throws CustomisedException {
//        User toSendTo = userRepository.getUserById(messageRecord.sendTo());
//        if(toSendTo == null){
//            throw new CustomisedException("User doesn't exist!");
//        }
//        Message message = Message.builder()
//                .senderId(Utils.getCurrentUser())
//                .receiverId(userRepository.getUserById(messageRecord.sendTo()))
//                .message(messageRecord.message())
//                .status(MessageStatus.SENT)
//                .build();
//        Message savedMessage = messageRepository.save(message);
//        messagingTemplate.convertAndSendToUser(String.valueOf(savedMessage.getReceiverId()),
//                "/queue/messages",
//                MessageNotification.builder()
//                        .receiverId(savedMessage.getReceiverId().getId())
//                        .senderId(savedMessage.getSenderId().getId())
//                        .build());
//    }
    @RequestMapping(value = "/{from}/{to}",method = RequestMethod.GET)
    @APIResult(message = " ", error_message = " ", message_code = 0)
    public Object getMessages(@PathVariable(value = "from") String from, @PathVariable(value = "to") String to){
        return messageService.getMessages(Long.valueOf(from), Long.valueOf(to));
    }

    @RequestMapping(value = "/{from}/{to}/count",method = RequestMethod.GET)
    @APIResult(message = " ", error_message = " ", message_code = 0)
    public Object getCountOfNewMessages(@RequestParam("from") String from, @RequestParam("to") String to){
        return messageService.getNewMessagesCount(Long.valueOf(from), Long.valueOf(to));
    }
}
