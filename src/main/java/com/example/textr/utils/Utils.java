package com.example.textr.utils;

import com.example.textr.dto.SubjectDto;
import com.example.textr.entity.User;
import com.example.textr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    @Autowired
    static UserRepository userRepository;

    public static long getCurrentUserId() {
        SubjectDto subjectDto = (SubjectDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return subjectDto.getId();
    }

    public static SubjectDto getCurrentUserDetails() {
        SubjectDto subjectDto = (SubjectDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return subjectDto;
    }

    public static User getCurrentUser() {
        Long currentUserId = getCurrentUserId();
        User user = userRepository.getUserById(currentUserId);
        return user;
    }
}
