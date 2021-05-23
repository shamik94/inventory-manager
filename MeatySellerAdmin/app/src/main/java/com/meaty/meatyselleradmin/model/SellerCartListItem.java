package com.meaty.meatyselleradmin.model;

public class SellerCartListItem {
    private String fishId;
    private String fishName;
    private Double weightInKg;
    private Boolean isChecked;

    public SellerCartListItem(String fishId, String fishName, Boolean isChecked) {
        this.fishId = fishId;
        this.fishName = fishName;
        this.weightInKg = 0.0;
        this.isChecked = isChecked;
    }

    public SellerCartListItem() {
    }

    public String getFishId() {
        return fishId;
    }

    public void setFishId(String fishId) {
        this.fishId = fishId;
    }

    public String getFishName() {
        return fishName;
    }

    public void setFishName(String fishName) {
        this.fishName = fishName;
    }

    public Double getWeightInKg() {
        return weightInKg;
    }

    public void setWeightInKg(Double weightInKg) {
        this.weightInKg = weightInKg;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
