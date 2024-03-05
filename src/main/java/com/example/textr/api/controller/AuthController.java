package com.example.textr.api.controller;

import com.example.textr.annotation.APIResult;
import com.example.textr.api.service.AuthService;
import com.example.textr.exception.CustomisedException;
import com.example.textr.records.LoginResponseRecord;
import com.example.textr.records.LoginUser;
import com.example.textr.records.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.data.rest.base.path}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @APIResult(message = "Auth Worked", error_message = "Auth Failed", message_code = 0)
    public Object login(@RequestBody LoginUser loginUser) throws Exception {
        return authService.login(loginUser);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @APIResult(message = "Registration Successful", error_message = "Registration Failed", message_code = 0)
    public Object register(@RequestBody User user) throws CustomisedException {
        return authService.registerAccount(user);
    }
}
