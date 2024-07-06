package com.example.pawpal10;

public class DogFoodItem {
    private int imageResource;
    private String name;
    private String price;
    private float rating;
    private int reviewCount;

    public DogFoodItem(int imageResource, String name, String price, float rating, int reviewCount) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }
}


