package com.android.carrentsystem.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;

public class RentOrder implements Parcelable {

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
    private long numDays;
    private double totalPrice;
    private String agencyNotes;

    public RentOrder() {
    }

    protected RentOrder(Parcel in) {
        id = in.readString();
        agencyId = in.readString();
        fullName = in.readString();
        civilId = in.readString();
        phone = in.readString();
        licenseImages = in.createStringArrayList();
        selectedCar = in.readParcelable(Car.class.getClassLoader());
        status = in.readString();
        from = in.readLong();
        to = in.readLong();
        numDays = in.readLong();
        totalPrice = in.readDouble();
        agencyNotes = in.readString();
    }

    public static final Creator<RentOrder> CREATOR = new Creator<RentOrder>() {
        @Override
        public RentOrder createFromParcel(Parcel in) {
            return new RentOrder(in);
        }

        @Override
        public RentOrder[] newArray(int size) {
            return new RentOrder[size];
        }
    };

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

    public long getNumDays() {
        return numDays;
    }

    public void setNumDays(long numDays) {
        this.numDays = numDays;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAgencyNotes() {
        return agencyNotes;
    }

    public void setAgencyNotes(String agencyNotes) {
        this.agencyNotes = agencyNotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(agencyId);
        dest.writeString(fullName);
        dest.writeString(civilId);
        dest.writeString(phone);
        dest.writeStringList(licenseImages);
        dest.writeParcelable(selectedCar, flags);
        dest.writeString(status);
        dest.writeLong(from);
        dest.writeLong(to);
        dest.writeLong(numDays);
        dest.writeDouble(totalPrice);
        dest.writeString(agencyNotes);
    }

    public enum RentOrderStatus {
        NEW("new"),
        PROCESSING("processing"),
        CONFIRMED("confirmed"),
        REJECTED("rejected"),
        RETURNED("returned");

        public final String value;
        RentOrderStatus(String status) {
            value = status;
        }
    }
}
