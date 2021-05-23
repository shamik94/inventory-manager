package com.meaty.seller.model.firebase;

public class SellerProfile {

    private String sellerId;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String govtId;
    private String photoPath;
    private String govtIdPath;

    public SellerProfile() {}

    public SellerProfile(String firstName,
                         String lastName,
                         String address,
                         String phoneNumber,
                         String govtId,
                         String photoPath,
                         String govtIdPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.govtId = govtId;
        this.photoPath = photoPath;
        this.govtIdPath = govtIdPath;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGovtId() {
        return govtId;
    }

    public void setGovtId(String govtId) {
        this.govtId = govtId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getGovtIdPath() {
        return govtIdPath;
    }

    public void setGovtIdPath(String govtIdPath) {
        this.govtIdPath = govtIdPath;
    }
}
