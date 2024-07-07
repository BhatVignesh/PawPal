package com.example.pawpal10;

public class CartDetailsModel {

    String product_name;
    String product_Id;
    int product_price;
    int quantity;
    public CartDetailsModel(){

    }

    public CartDetailsModel(String product_name, String product_Id, int product_price, int quantity) {
        this.product_name = product_name;
        this.product_Id = product_Id;
        this.product_price = product_price;
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(String product_Id) {
        this.product_Id = product_Id;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
