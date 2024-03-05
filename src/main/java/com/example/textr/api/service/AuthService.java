package com.example.textr.api.service;

import com.example.textr.exception.CustomisedException;
import com.example.textr.records.LoginResponseRecord;
import com.example.textr.records.LoginUser;
import com.example.textr.records.User;

public interface AuthService {
    public LoginResponseRecord login(LoginUser loginUser) throws Exception;

    public User registerAccount(User user) throws CustomisedException;
}