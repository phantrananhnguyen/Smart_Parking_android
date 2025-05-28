package com.example.smartparking.Models;

public class TicketItem {
    private int amount;
    private String start;
    private String end;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }



    public TicketItem(int amount, String start, String end) {
        this.amount = amount;
        this.start = start;
        this.end = end;
    }
}
