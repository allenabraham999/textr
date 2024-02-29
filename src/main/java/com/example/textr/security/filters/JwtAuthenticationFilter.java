package com.example.textr.security.filters;

import com.example.textr.api.service.implementation.UserServiceImpl;
import com.example.textr.dto.ResultDto;
import com.example.textr.dto.SubjectDto;
import com.example.textr.security.utils.JwtTokenUtils;
import com.example.textr.utils.Constants;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Order(3)
@WebFilter("/secure/**")
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(Constants.AUTH_HEADER_KEY);
        String redisKey = null;
        String authToken = null;
        String url = request.getRequestURI();
        if(!url.contains("/secure/")){
            filterChain.doFilter(request, response);
            return;
        }

        if(header!=null && header.startsWith(Constants.TOKEN_PREFIX)){
            authToken = header.replace(Constants.TOKEN_PREFIX, "");
            try {
                redisKey = jwtTokenUtils.extractTokenValue(authToken);
                if (redisKey != null) {
                    SubjectDto subjectDto = jwtTokenUtils.getSubject(redisKey);
                    if (jwtTokenUtils.validateToken(subjectDto)) {
                        UserServiceImpl userService = (UserServiceImpl) userDetailsService;
                        try {
                            //Handle user deleted
                            userService.findById(subjectDto.getId());
                        } catch (EmptyResultDataAccessException e) {
                            jwtTokenUtils.deleteSession(redisKey);
                            throw new UnsupportedJwtException(e.getMessage());
                        }
                        List<GrantedAuthority> roles = new ArrayList<>();
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                subjectDto, null, roles);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        sendErrorWithStatus(HttpStatus.UNAUTHORIZED.value(), "Session expired", response);
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                sendErrorWithStatus(HttpStatus.FORBIDDEN.value(), "Unauthorized", response);
                return;
            } catch (ExpiredJwtException e) {
                sendErrorWithStatus(HttpStatus.UNAUTHORIZED.value(), "Session expired", response);
                return;
            } catch (SignatureException e) {
                sendErrorWithStatus(HttpStatus.FORBIDDEN.value(), "Invalid signature", response);
                return;
            } catch(UnsupportedJwtException | MalformedJwtException e) {
                sendErrorWithStatus(HttpStatus.FORBIDDEN.value(), "Unauthorized", response);
                return;
            }
        } else {
            sendErrorWithStatus(HttpStatus.FORBIDDEN.value(), "Unauthorized", response);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorWithStatus(HttpStatus.FORBIDDEN.value(), "Unauthorized", response);
        }
    }

    private void sendErrorWithStatus(int status, String message, HttpServletResponse res) {
        res.setStatus(status);
        try {
            Writer writer = res.getWriter();
            writer.write(new Gson().toJson(new ResultDto(false, message, null)));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

//TODO: think about adding the extra files as per the base framework