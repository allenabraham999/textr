package com.example.textr.aspect;

import com.example.textr.annotation.APIResult;
import com.example.textr.dto.ResultDto;
import com.example.textr.exception.CustomisedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ResourceBundle;

@Aspect
@Component
public class ResultAspect {

    @Autowired
    private ResourceBundle resourceBundle;

    @Around("@annotation(com.example.textr.annotation.APIResult)")
    public Object onApiResponse(final ProceedingJoinPoint pjp) {
        final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        APIResult apiResult = method.getAnnotation(APIResult.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        ResultDto result = null;
        RequestMethod[] requestMethod = requestMapping.method();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            final Object returnValue = pjp.proceed();
            if (returnValue == null) {
                result = new ResultDto(true, retrieveMessage(apiResult.error_message()), returnValue);
                httpStatus = HttpStatus.I_AM_A_TEAPOT;
            } else {
                result = new ResultDto(true, retrieveMessage(apiResult.message()), apiResult.message_code(),
                        returnValue);
                httpStatus = getStatusForHttpType(requestMethod[0]);
            }
        } catch (Throwable e) {
            if (e instanceof CustomisedException) {
                CustomisedException exception = (CustomisedException) e;
                result = new ResultDto(false, retrieveMessage(exception.getMessage()), exception.getMessageCode(),
                        null);
            }
//            if (e instanceof OTException) {
//                OTException exception = (OTException) e;
//                result = new ResultDto(false, retrieveMessage(exception.getMessage()), exception.getMessageCode(),
//                        null);
//            }
            else if (e instanceof EmptyResultDataAccessException) {
                httpStatus = HttpStatus.NOT_FOUND;
                result = new ResultDto(true, retrieveMessage("#data.not.found"), 0, null);
            }
//            else if(e instanceof InvalidFpTokenException) {
//                httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
//                InvalidFpTokenException exception = (InvalidFpTokenException) e;
//                result = new ResultDto(false, retrieveMessage(exception.getMessage()), exception.getMessageCode(), null);
//            } else if (e instanceof UnAuthorizedExceptioU) {
//                httpStatus = HttpStatus.UNAUTHORIZED;
//                UnAuthorizedException exception = (UnAuthorizedException) e;
//                result = new ResultDto(false, retrieveMessage(exception.getMessage()), 0, null);
//            }
            else {
                e.printStackTrace();
                result = new ResultDto(false, retrieveMessage("#server.error"), 0, null);
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<Object>(result, httpStatus);
    }

    private String retrieveMessage(String key) {
        if (key != null && key.startsWith("#")) {
            return resourceBundle.getString(key.replace("#", ""));
        }
        return key;
    }

    private HttpStatus getStatusForHttpType(RequestMethod requestMethod) {
        HttpStatus status = null;
        switch (requestMethod) {
            case GET:
                status = HttpStatus.OK;
                break;
            case POST:
                status = HttpStatus.CREATED;
                break;
            case PUT:
                status = HttpStatus.CREATED;
                break;
            case PATCH:
                status = HttpStatus.OK;
                break;
            case DELETE:
                status = HttpStatus.OK;
                break;
            default:
                break;
        }
        return status;
    }

}
