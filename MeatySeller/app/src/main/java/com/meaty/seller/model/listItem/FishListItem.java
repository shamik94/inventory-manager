package com.meaty.seller.model.listItem;

public class FishListItem {

    private String fishId;
    private String fishName;
    private Double fishPrice;
    private Integer imageId;
    private Boolean isChecked;

    public FishListItem(String fishId, String fishName, Double fishPrice, Integer imageId, Boolean isChecked) {
        this.fishName = fishName;
        this.fishId = fishId;
        this.fishPrice = fishPrice;
        this.imageId = imageId;
        this.isChecked = isChecked;
    }

    public String getFishName() {
        return fishName;
    }

    public void setFishName(String fishName) {
        this.fishName = fishName;
    }

    public Double getFishPrice() {
        return fishPrice;
    }

    public void setFishPrice(Double fishPrice) {
        this.fishPrice = fishPrice;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getPriceString() {
        return "Rs " + this.fishPrice + " per Kg";
    }

    public String getFishId() {
        return fishId;
    }

    public void setFishId(String fishId) {
        this.fishId = fishId;
    }
}
