package com.meaty.seller.model.firebase;

import java.io.Serializable;

public class Fish implements Serializable {

    private String id;
    private String name;
    private String image;
    private Double price;

    public Fish(){}

    public Fish(String name, String image, Double price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
