package com.example.pawpal10;

public class DogFoodItem {

    int pid;
    String product_type;
    String description;
    private String imageUrl;
    private String name;
    private String price;
    private float rating;
    private int reviewCount;
    String size;

    public DogFoodItem(String imageUrl, String name, String price, float rating, int reviewCount, int pid, String product_type, String description,String size) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.pid = pid;
        this.product_type = product_type;
        this.description = description;
        this.size=size;
    }

    public int getPid() {
        return pid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getImageUrl() {
        return imageUrl;
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



