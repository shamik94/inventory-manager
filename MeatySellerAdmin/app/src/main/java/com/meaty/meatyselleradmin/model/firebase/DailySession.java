package com.meaty.meatyselleradmin.model.firebase;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DailySession implements Serializable {

    private String dailySessionId;
    private String sellerId;
    private Map<String, Double> startInventory;
    private Boolean active;
    private List<GeoPoint> location;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public DailySession() {

    }

    public DailySession(String sellerId,
                        String dailySessionId,
                        Map<String, Double> startInventory,
                        Boolean active,
                        List<GeoPoint> location,
                        Timestamp createdAt,
                        Timestamp updatedAt) {
        this.dailySessionId = dailySessionId;
        this.sellerId = sellerId;
        this.startInventory = startInventory;
        this.active = active;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getDailySessionId() {
        return dailySessionId;
    }

    public void setDailySessionId(String dailySessionId) {
        this.dailySessionId = dailySessionId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Map<String, Double> getStartInventory() {
        return startInventory;
    }

    public void setStartInventory(Map<String, Double> startInventory) {
        this.startInventory = startInventory;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<GeoPoint> getLocation() {
        return location;
    }

    public void setLocation(List<GeoPoint> location) {
        this.location = location;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
