package com.java.spring.grpc.to;

public class ProductInputTo {

    private final String name;
    private final double price;
    private final int quantity;

    public ProductInputTo(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
