package com.java.spring.grpc.service;

import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
import java.util.List;

public interface ProductService {
    ProductOutputTo save(ProductInputTo to);
    ProductOutputTo findById(long id);
    List<ProductOutputTo> findAll();
    void delete(long id);
}
