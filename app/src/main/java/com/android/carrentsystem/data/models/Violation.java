package com.android.carrentsystem.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Violation implements Parcelable {

    private String notes;
    private double cost;
    private String date;

    public Violation() {
    }

    protected Violation(Parcel in) {
        notes = in.readString();
        cost = in.readDouble();
        date = in.readString();
    }

    public static final Creator<Violation> CREATOR = new Creator<Violation>() {
        @Override
        public Violation createFromParcel(Parcel in) {
            return new Violation(in);
        }

        @Override
        public Violation[] newArray(int size) {
            return new Violation[size];
        }
    };

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(notes);
        dest.writeDouble(cost);
        dest.writeString(date);
    }
}
