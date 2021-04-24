package com.example.restaurant;

public class Restaurants {

    public String name;
    public double ratings, longitude, latitude;
    public int reviews;

    public Restaurants(){}

    public Restaurants(String name, Double ratings, Double longitude, Double latitude, int reviews){

        this.name = name;
        this.ratings = ratings;
        this.reviews = reviews;
        this.longitude = longitude;
        this.latitude = latitude;

    }

}
