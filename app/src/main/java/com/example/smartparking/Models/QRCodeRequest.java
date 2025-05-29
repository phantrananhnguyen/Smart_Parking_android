package com.example.smartparking.Models;

public class QRCodeRequest {
    private String carPlate;
    private int months;
    private String email;

    public QRCodeRequest(String carPlate, int months, String email) {
        this.carPlate = carPlate;
        this.months = months;
        this.email = email;
    }
}

