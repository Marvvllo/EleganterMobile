package com.marvello.eleganter;

public class Furniture {
    public String key, name, image, seller, specs;

    public Furniture(String name, String image, String seller, String specs) {
        this.name = name;
        this.image = image;
        this.seller = seller;
        this.specs = specs;
    }

    public Furniture() {

    }

    public String getCode() {
        return key;
    }

    public void setCode(String key) {
        this.key = key;
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

    public String getSeller() {
        return seller;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

}
