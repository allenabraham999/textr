package com.example.textr.api.controller;

import com.example.textr.api.service.AuthService;
import com.example.textr.records.LoginUser;
import com.example.textr.records.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${spring.data.rest.base.path}")
public class AuthController {
    @Autowired
    AuthService authService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public User login(@RequestBody LoginUser loginUser){
        return authService.login(loginUser);
    }
}
