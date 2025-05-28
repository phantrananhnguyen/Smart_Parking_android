package com.example.smartparking.Models;

import com.google.gson.annotations.SerializedName;

public class MonthTicket {
    @SerializedName("licensePlate")
    private String carPlate;

    @SerializedName("car_company")
    private String carBrand;

    @SerializedName("owner")
    private String ownerName;

    @SerializedName("email")
    private String email;

    @SerializedName("amount")
    private int monthAmount;

    @SerializedName("start")
    private String startDate;

    @SerializedName("end")
    private String endDate;

    // Constructor
    public MonthTicket(String carPlate, String carBrand, String ownerName, String email, int monthAmount, String startDate, String endDate) {
        this.carPlate = carPlate;
        this.carBrand = carBrand;
        this.ownerName = ownerName;
        this.email = email;
        this.monthAmount = monthAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Empty constructor (nên có cho Gson)
    public MonthTicket() {
    }

    // Getters và Setters
    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(int monthAmount) {
        this.monthAmount = monthAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "MonthTicket{" +
                "carPlate='" + carPlate + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", email='" + email + '\'' +
                ", monthAmount=" + monthAmount +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
