package com.ratelimiting.ratelimiting.models;

public class AreaV1 {
    private Double area;
    private String shape;

    public AreaV1(String shape, Double area) {
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
