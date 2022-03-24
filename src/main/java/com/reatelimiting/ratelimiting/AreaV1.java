package com.reatelimiting.ratelimiting;

class AreaV1 {
    private Integer area;
    private String shape;

    public AreaV1(String shape, Integer area) {
        this.shape = shape;
        this.area = area;
    }

    public Integer getArea() {
        return area;
    }

    public String getShape() {
        return shape;
    }
}
