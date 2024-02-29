package com.example.textr.exception;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomisedException extends Exception{
    private String message;

    private int messageCode;

    private String causeMessage = "";
    public CustomisedException(String message) {
        this.message = message;
    }

    public CustomisedException(String message, int messageCode) {
        this.message = message;
        this.setMessageCode(messageCode);
    }

}
