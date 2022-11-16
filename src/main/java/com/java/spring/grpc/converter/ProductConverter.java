package com.java.spring.grpc.converter;

import com.java.spring.grpc.domain.Product;
import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;

public class ProductConverter {

    private ProductConverter() { /* TODO document why this constructor is empty */ }

    public static ProductOutputTo productToProductOutputTo(Product product) {
        return new ProductOutputTo(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    public static Product productInputToProduct(ProductInputTo product) {
        return new Product(null, product.getName(), product.getPrice(), product.getQuantity());
    }
}
