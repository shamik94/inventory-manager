package com.meaty.seller.model.firebase;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class Sales {

    private String sellerId;
    private Map<String, Double> fishKgMap;
    private Map<String, Double> fishSalesMap;
    private GeoPoint location;
    private Double totalSale;
    private Timestamp updatedAt;

    public Sales() {
    }

    public Sales(String sellerId, Map<String, Double> fishKgMap, Map<String, Double> fishSalesMap, GeoPoint location, Double totalSale, Timestamp updatedAt) {
        this.sellerId = sellerId;
        this.fishKgMap = fishKgMap;
        this.fishSalesMap = fishSalesMap;
        this.location = location;
        this.totalSale = totalSale;
        this.updatedAt = updatedAt;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Map<String, Double> getFishKgMap() {
        return fishKgMap;
    }

    public void setFishKgMap(Map<String, Double> fishKgMap) {
        this.fishKgMap = fishKgMap;
    }

    public Map<String, Double> getFishSalesMap() {
        return fishSalesMap;
    }

    public void setFishSalesMap(Map<String, Double> fishSalesMap) {
        this.fishSalesMap = fishSalesMap;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
