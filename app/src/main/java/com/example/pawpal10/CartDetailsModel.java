package com.example.pawpal10;

public class CartDetailsModel {

    String product_name;
    Long product_price;
    int quantity;

    public CartDetailsModel(String product_name, Long product_price, int quantity) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
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
