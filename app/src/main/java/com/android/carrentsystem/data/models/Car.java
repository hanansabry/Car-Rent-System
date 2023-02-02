package com.android.carrentsystem.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;

public class Car implements Parcelable {

    private String id;
    private String agencyName;
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

    public Car() {
    }

    protected Car(Parcel in) {
        id = in.readString();
        agencyName = in.readString();
        category = in.readString();
        type = in.readString();
        model = in.readString();
        year = in.readString();
        color = in.readString();
        platNum = in.readString();
        description = in.readString();
        price = in.readDouble();
        carImagesUrls = in.createStringArrayList();
        status = in.readString();
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(agencyName);
        dest.writeString(category);
        dest.writeString(type);
        dest.writeString(model);
        dest.writeString(year);
        dest.writeString(color);
        dest.writeString(platNum);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeStringList(carImagesUrls);
        dest.writeString(status);
    }

    public enum CarStatus {
        AVAILABLE("available"), RENTED("rented"), NOT_AVAILABLE("not_available");

        public final String value;

        CarStatus(String status) {
            value = status;
        }
    }
}
