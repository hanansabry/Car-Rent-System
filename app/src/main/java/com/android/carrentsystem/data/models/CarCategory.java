package com.android.carrentsystem.data.models;

import java.util.List;

public class CarCategory {

    private String name;
    private List<CarType> types;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CarType> getTypes() {
        return types;
    }

    public void setTypes(List<CarType> types) {
        this.types = types;
    }
}
