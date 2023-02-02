package com.android.carrentsystem.data.models;

import java.util.List;

public class RentOrder {

    private String id;
    private String agencyId;
    private String fullName;
    private String civilId;
    private String phone;
    private List<String> licenseImages;
    private Car selectedCar;
    private String status;
    private long from;
    private long to;
    private double totalPrice;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getLicenseImages() {
        return licenseImages;
    }

    public void setLicenseImages(List<String> licenseImages) {
        this.licenseImages = licenseImages;
    }

    public Car getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public enum RentOrderStatus {
        NEW("new"), PENDING("pending"), CONFIRMED("confirmed");

        public final String value;
        RentOrderStatus(String status) {
            value = status;
        }
    }
}
