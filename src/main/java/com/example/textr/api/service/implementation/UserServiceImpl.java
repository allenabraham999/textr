package com.example.textr.api.service.implementation;

import com.example.textr.api.service.UserService;
import com.example.textr.entity.User;
import com.example.textr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(username);
        List<GrantedAuthority> roles = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.getUserByEmail(username);
//				return userRepository.findByEmployeeEmail(username);
//						.orElseThrow(() -> new UsernameNotFoundException("user not found"));
                List<SimpleGrantedAuthority> role = new ArrayList<>();
                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), role);
            }
        };
    }
    @Bean
    public ResourceBundle resourceBundle() {
        // You may need to adjust the base name according to your resource bundle structure
        return ResourceBundle.getBundle("message");
    }

}