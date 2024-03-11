package com.example.textr.api.service.implementation;

import com.example.textr.api.service.MessageService;
import com.example.textr.entity.Message;
import com.example.textr.entity.User;
import com.example.textr.enums.MessageStatus;
import com.example.textr.exception.CustomisedException;
import com.example.textr.records.MessageRecord;
import com.example.textr.repository.MessageRepository;
import com.example.textr.repository.UserRepository;
import com.example.textr.utils.Utils;
import com.example.textr.utils.mappers.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public MessageRecord sendMessage(String text, Long id) throws CustomisedException {
        Long userId = Utils.getCurrentUserId();
        User sender = userRepository.getUserById(userId);
        User receiver = userRepository.getUserById(id);
        if(receiver == null){
            throw new CustomisedException("Cant send as receiver doesn't exist!");
        }
        if(receiver == sender){
            throw new CustomisedException("Cant send message to yourself!");
        }
        Message message = Message.builder()
                .senderId(sender)
                .receiverId(receiver)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .message(text)
                .status(MessageStatus.SENT)
                .build();
        messageRepository.save(message);
        return MessageMapper.messageToMessageRecord(message);
    }

    @Override
    public List<MessageRecord> getMessages(Long from, Long to){
        List<Message> messages = messageRepository.getMessages(from,to);
        messages.forEach(m->{
            m.setStatus(MessageStatus.READ);
        });
        List<MessageRecord> records = MessageMapper.messageToMessageRecord(messages);
        return records;
    }

    @Override
    public Long getNewMessagesCount(Long from, Long to){
        return messageRepository.getNewMessagesCount(from, to);
    }
}
