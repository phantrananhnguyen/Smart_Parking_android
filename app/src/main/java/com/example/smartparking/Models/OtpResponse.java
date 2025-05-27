package com.example.smartparking.Models;

public class OtpResponse {
    private String message;
    private String error;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    // Kiểm tra xem phản hồi có thành công không
    public boolean isSuccess() {
        return message != null && error == null;
    }
}