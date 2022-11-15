package com.java.spring.grpc.to;

public class ProductOutputTo {

    private final Long id;
    private final String name;
    private final double price;
    private final int quantity;

    public ProductOutputTo(Long id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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
