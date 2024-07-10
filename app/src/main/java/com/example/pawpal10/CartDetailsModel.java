package com.example.pawpal10;

public class CartDetailsModel {
    String product_name;
    Long product_price;
    int quantity;
    String imageUrl;
    String size;
    Long total_price;

    public CartDetailsModel(String product_name, Long product_price, int quantity,String imageUrl,String size,Long total_price) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.imageUrl=imageUrl;
        this.size=size;
        this.total_price=total_price;
    }

    public Long getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Long total_price) {
        this.total_price = total_price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Long getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Long product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
