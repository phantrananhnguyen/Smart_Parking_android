package com.example.smartparking.Models;

public class ProfileResponse {
    String message;
    String join;
    String plate;
    int numberOfTickets;
    String latestTicketStatus;

    public String getMessage() {
        return message;
    }

    public String getJoin() {
        return join;
    }

    public String getPlate() {
        return plate;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public String getLatestTicketStatus() {
        return latestTicketStatus;
    }

    public ProfileResponse(String message, String join, String plate, int numberOfTickets, String latestTicketStatus) {
        this.message = message;
        this.join = join;
        this.plate = plate;
        this.numberOfTickets = numberOfTickets;
        this.latestTicketStatus = latestTicketStatus;
    }
}
