package com.example.smartparking.Models;

public class QrData {
    private String carPlate;
    private int months;
    private int totalAmount;
    private String qrData;
    private String qrImage;
    private PaymentInfo paymentInfo;

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    public String getQrImage() {
        return qrImage;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public QrData(String carPlate, int months, int totalAmount, String qrData, String qrImage, PaymentInfo paymentInfo) {
        this.carPlate = carPlate;
        this.months = months;
        this.totalAmount = totalAmount;
        this.qrData = qrData;
        this.qrImage = qrImage;
        this.paymentInfo = paymentInfo;
    }
}
