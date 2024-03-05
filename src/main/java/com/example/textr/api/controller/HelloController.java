package com.example.textr.api.controller;

import com.example.textr.annotation.APIResult;
import com.example.textr.api.service.HelloService;
import com.example.textr.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.data.rest.base.path}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HelloController {

    @Autowired
    HelloService helloService;



    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @APIResult(message = "hello worked", error_message ="hello failed" , message_code = 0)
    public Object test(){
        return helloService.getHelloInfo();
    }
}
