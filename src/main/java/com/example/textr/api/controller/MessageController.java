package com.example.textr.api.controller;

import com.example.textr.annotation.APIResult;
import com.example.textr.api.service.MessageService;
import com.example.textr.records.MessageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.data.rest.base.path}"+"/secure/message")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {
    @Autowired
    MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    @APIResult(message = " ",error_message = " ", message_code = 0)
    public Object sendMessage(@RequestBody MessageRecord message){
        return messageService.sendMessage(message.message(), message.sendTo());
    }

    @RequestMapping(method = RequestMethod.GET)
    @APIResult(message = " ", error_message = " ", message_code = 0)
    public Object getMessages(@RequestParam("from") Long from, @RequestParam("to") Long to){
        return messageService.getMessages(from, to);
    }

}
