package com.example.textr.api.service;

import com.example.textr.records.LoginUser;
import com.example.textr.records.User;

public interface AuthService {
    public User login(LoginUser loginUser);

}
