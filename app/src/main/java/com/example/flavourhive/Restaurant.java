package com.example.flavourhive;

public class Restaurant {

    private String name;
    private String type;
    private float rating;
    private String address;

    public Restaurant(String name, String type, float rating, String address) {
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.address = address;
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

    public String getAddress(){ return address; }

    public void setAddress(String address){ this.address = address; }
}
