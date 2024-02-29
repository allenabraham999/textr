package com.example.textr.api.service.implementation;

import com.example.textr.api.service.AuthService;
import com.example.textr.records.LoginUser;
import com.example.textr.records.User;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public User login(LoginUser loginUser) {
        return null;
    }
}
