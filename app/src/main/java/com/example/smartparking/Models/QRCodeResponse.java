package com.example.smartparking.Models;

public class QRCodeResponse {
    private String message;
    private QrData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public QrData getData() {
        return data;
    }

    public void setData(QrData data) {
        this.data = data;
    }
}