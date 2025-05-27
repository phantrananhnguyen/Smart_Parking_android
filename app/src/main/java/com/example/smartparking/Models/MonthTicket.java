package com.example.smartparking.Models;

public class MonthTicket {
    String carPlate;
    String carBrand;
    String ownerName;
    String timeIn;
    int MonthAmount;

    public String getCarPlate() {
        return carPlate;
    }
    public MonthTicket(String carPlate, String carBrand, String ownerName, String timeIn, int amount) {
        this.carPlate = carPlate;
        this.carBrand = carBrand;
        this.ownerName = ownerName;
        this.timeIn = timeIn;
        this.MonthAmount = amount;
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

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

}
