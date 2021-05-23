package com.meaty.seller.model.listItem;

public class CheckoutListItem {

    private String fishId;
    private String fishName;
    private Double fishPrice;
    private Double salesKg;
    private Double salesGram;
    private Integer imageId;

    public CheckoutListItem(String fishId, String fishName, Double fishPrice, Integer imageId) {
        this.fishId = fishId;
        this.fishName = fishName;
        this.fishPrice = fishPrice;
        this.imageId = imageId;
        this.salesKg = 0.0;
        this.salesGram = 0.0;
    }

    public String getFishId() {
        return fishId;
    }

    public void setFishId(String fishId) {
        this.fishId = fishId;
    }

    public Double getFishPrice() {
        return fishPrice;
    }

    public void setFishPrice(Double fishPrice) {
        this.fishPrice = fishPrice;
    }

    public String getFishName() {
        return fishName;
    }

    public void setFishName(String fishName) {
        this.fishName = fishName;
    }

    public Double getSalesKg() {
        return salesKg;
    }

    public void setSalesKg(Double salesKg) {
        this.salesKg = salesKg;
    }

    public Double getSalesGram() {
        return salesGram;
    }

    public void setSalesGram(Double salesGram) {
        this.salesGram = salesGram;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
