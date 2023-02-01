package com.android.carrentsystem.data.models;

import java.util.List;

public class CarModel {

    private String name;
    private List<String> years;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }
}
