package com.android.carrentsystem.data.models;

import java.util.List;

public class Car {

    private String id;
    private String agencyId;
    private String category;
    private String type;
    private String model;
    private String year;
    private String color;
    private String platNum;
    private String description;
    private double price;
    private List<String> carImagesUrls;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlatNum() {
        return platNum;
    }

    public void setPlatNum(String platNum) {
        this.platNum = platNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getCarImagesUrls() {
        return carImagesUrls;
    }

    public void setCarImagesUrls(List<String> carImagesUrls) {
        this.carImagesUrls = carImagesUrls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public enum CarStatus {
        AVAILABLE("available"), RENTED("rented"), NOT_AVAILABLE("not_available");

        public final String value;

        CarStatus(String status) {
            value = status;
        }
    }
}
