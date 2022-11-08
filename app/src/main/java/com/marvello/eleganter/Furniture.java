package com.marvello.eleganter;

public class Furniture {
    public String key, name, image, brand, specs;

    public Furniture(String name, String image, String brand, String specs) {
        this.name = name;
        this.image = image;
        this.brand = brand;
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

    public String getBrand() {
        return brand;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

}
