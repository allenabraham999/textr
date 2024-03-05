package com.example.textr.api.service.implementation;

import com.example.textr.api.service.HelloService;
import com.example.textr.utils.Utils;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String getHelloInfo() {
        return "Hi I am";
    }
}
