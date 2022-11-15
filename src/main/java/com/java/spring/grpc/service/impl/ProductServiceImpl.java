package com.java.spring.grpc.service.impl;

import com.java.spring.grpc.converter.ProductConverter;
import com.java.spring.grpc.exception.AlreadyExistsException;
import com.java.spring.grpc.exception.NotFoundException;
import com.java.spring.grpc.repository.ProductRepository;
import com.java.spring.grpc.service.ProductService;
import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputTo save(ProductInputTo to) {
        this.checkDuplicity(to.getName());
        var newProduct = this.productRepository.save(ProductConverter.productInputToProduct(to));
        return ProductConverter.productToProductOutputTo(newProduct);
    }

    @Override
    public ProductOutputTo findById(long id) {
        return ProductConverter.productToProductOutputTo(
                this.productRepository.findById(id).orElseThrow(() -> new NotFoundException(id))
        );
    }

    @Override
    public Page<ProductOutputTo> findAll(Pageable pageable) {
        return this.productRepository.findAll(pageable).map(ProductConverter::productToProductOutputTo);
    }

    @Override
    public void delete(long id) {
        var product = this.productRepository.findById(id).orElseThrow();
        this.productRepository.delete(product);
    }

    private void checkDuplicity(String name) {
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> { throw new AlreadyExistsException(name); });
    }
}
