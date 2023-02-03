package com.android.carrentsystem.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RentDate implements Parcelable {

    private long from;
    private long to;
    private long daysNum;

    public RentDate() {
    }

    protected RentDate(Parcel in) {
        from = in.readLong();
        to = in.readLong();
        daysNum = in.readLong();
    }

    public static final Creator<RentDate> CREATOR = new Creator<RentDate>() {
        @Override
        public RentDate createFromParcel(Parcel in) {
            return new RentDate(in);
        }

        @Override
        public RentDate[] newArray(int size) {
            return new RentDate[size];
        }
    };

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

    public long getDaysNum() {
        return daysNum;
    }

    public void setDaysNum(long daysNum) {
        this.daysNum = daysNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(from);
        dest.writeLong(to);
        dest.writeLong(daysNum);
    }
}
