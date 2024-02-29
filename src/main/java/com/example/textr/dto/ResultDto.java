package com.example.textr.dto;

import java.io.Serializable;

public class ResultDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1589847317711024174L;

    private String message;

    private boolean status;

    private Object data;

    private int messageCode = 1;

    public ResultDto(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResultDto(boolean status, String message, int messageCode, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

}
