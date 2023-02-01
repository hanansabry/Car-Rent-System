package com.android.carrentsystem.data.models;

import java.util.List;

public class CarType {

    private String name;
    private List<CarModel> models;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CarModel> getModels() {
        return models;
    }
}
