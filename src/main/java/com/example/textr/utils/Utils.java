package com.example.textr.utils;

import com.example.textr.dto.SubjectDto;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    public static long getCurrentUserId() {
        SubjectDto subjectDto = (SubjectDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return subjectDto.getId();
    }

    public static SubjectDto getCurrentUserDetails() {
        SubjectDto subjectDto = (SubjectDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return subjectDto;
    }

}
