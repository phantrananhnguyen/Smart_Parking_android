package com.example.smartparking.Models;

import com.google.gson.annotations.SerializedName;

public class Slot {
    @SerializedName("slotId")
    private String slotId;

    @SerializedName("status")
    private String status;

    public Slot(String slotId, String status){
        this.slotId = slotId;
        this.status = status;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
