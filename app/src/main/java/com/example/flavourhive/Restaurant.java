package com.example.flavourhive;

public class Restaurant {

    private String name;
    private String type;
    private float rating;

    public Restaurant(String name, String type, float rating) {
        this.name = name;
        this.type = type;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
