package com.example.textr.api.service.implementation;

import com.example.textr.api.service.AuthService;
import com.example.textr.config.SecurityConfig;
import com.example.textr.dto.UserDto;
import com.example.textr.exception.CustomisedException;
import com.example.textr.records.LoginResponseRecord;
import com.example.textr.records.LoginUser;
import com.example.textr.records.User;
import com.example.textr.repository.UserRepository;
import com.example.textr.security.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Override
    public LoginResponseRecord login(LoginUser loginUser) throws Exception {
        com.example.textr.entity.User user = userRepository.getUserByEmail(loginUser.email());
        if(user==null){
            throw new CustomisedException("User not there!");
        }
        LoginResponseRecord record = null;
        try{
            AuthenticationManager authenticationManager = securityConfig.authenticationManager(new AuthenticationConfiguration());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.email(),loginUser.password()));
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
            String token = jwtTokenUtils.generateToken(userDto);
            record = LoginResponseRecord.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .token(token)
                    .build();

        }catch (BadCredentialsException e){
            throw new CustomisedException("Failed due to bad credentials");
        } catch (Exception e){
            throw new CustomisedException("Failed Auth");
        }
        return record;
    }
}
