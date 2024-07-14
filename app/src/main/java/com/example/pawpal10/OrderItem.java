package com.example.pawpal10;

import java.util.List;

public class OrderItem {
    private String date;
    private List<String> products;
    private double totalAmount;

    // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    public OrderItem() {
    }

    public OrderItem(String date, List<String> products, double totalAmount) {
        this.date = date;
        this.products = products;
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}

