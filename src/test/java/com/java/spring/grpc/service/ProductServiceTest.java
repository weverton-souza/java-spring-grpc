package com.java.spring.grpc.service;

import com.java.spring.grpc.domain.Product;
import com.java.spring.grpc.exception.AlreadyExistsException;
import com.java.spring.grpc.repository.ProductRepository;
import com.java.spring.grpc.service.impl.ProductServiceImpl;
import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    @DisplayName("Create a new product")
    void createANewProductSuccessTest() {
        var product = new Product(1L, "Product Name", 10.0, 8);

        when(productRepository.save(any())).thenReturn(product);

        var input = new ProductInputTo("Product Name", 10.0, 8);

        var output = this.productServiceImpl.save(input);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(output);
    }

    @Test
    @DisplayName("Find a product by Id")
    void findAProductByIdProductSuccessTest() {
        var product = new Product(1L, "Product Name", 10.0, 8);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var output = this.productServiceImpl.findById(1L);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(output);
    }

    @Test
    @DisplayName("AlreadyExistsException when product with duplicate name.")
    void createANewProductAlreadyExistsExceptionTest() {
        var product = new Product(1L, "Product Name", 10.0, 8);

        when(productRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(product));

        var input = new ProductInputTo("Product Name", 10.0, 8);

        Assertions
                .assertThatExceptionOfType(AlreadyExistsException.class)
                .isThrownBy(() -> this.productServiceImpl.save(input));
    }

}
