package com.example.textr.aspect;

import com.example.textr.dto.ResultDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
     */

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//		return super.handleMethodArgumentNotValid(ex, headers, status, request);
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }
        ResultDto result = new ResultDto(false, errors.isEmpty()? "Method Argument Not Valid" : errors.get(0), 0);
        return new ResponseEntity<>(result, status);
    }

//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		List<String> errors = new ArrayList<String>();
//	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//	        errors.add(error.getDefaultMessage());
//	    }
//	    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
//	        errors.add(error.getDefaultMessage());
//	    }
//	    ResultDto result = new ResultDto(false, errors.isEmpty()? "Method Argument Not Valid" : errors.get(0), 0);
//		return new ResponseEntity<>(result, status);
//	}

}
