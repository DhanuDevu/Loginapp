package com.reglog.auth.dto;

public class TokenValidationResponse {

    private boolean valid;
    private String username;
    private Long uid;
    private String message;

    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean valid, String username, Long uid, String message) {
        this.valid = valid;
        this.username = username;
        this.uid = uid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
