package com.java.spring.grpc.service;

import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductOutputTo save(ProductInputTo to);
    ProductOutputTo findById(long id);
    Page<ProductOutputTo> findAll(Pageable pageable);
    void delete(long id);
}
