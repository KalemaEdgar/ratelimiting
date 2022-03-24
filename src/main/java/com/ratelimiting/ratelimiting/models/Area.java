package com.ratelimiting.ratelimiting.models;

public class Area {
    private Double area;
    private String shape;

    public Area(String shape, Double area) {
        this.shape = shape;
        this.area = area;
    }

    public Double getArea() {
        return area;
    }

    public String getShape() {
        return shape;
    }
}
