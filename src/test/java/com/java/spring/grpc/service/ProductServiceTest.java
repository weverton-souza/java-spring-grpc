package com.java.spring.grpc.service;

import com.java.spring.grpc.domain.Product;
import com.java.spring.grpc.repository.ProductRepository;
import com.java.spring.grpc.service.impl.ProductServiceImpl;
import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    @DisplayName("Create a new product")
    public void createANewProductSuccessTest() {
        var product = new Product(1L, "Product Name", 10.0, 8);

        Mockito.when(productRepository.save(any())).thenReturn(product);

        var input = new ProductInputTo("Product Name", 10.0, 8);

        var output = this.productServiceImpl.save(input);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(output);
    }

//    @Test
//    @DisplayName("Find a product by Id")
//    public void findAProductByIdProductSuccessTest() {
//        var product = new Product(1L, "Product Name", 10.0, 8);
//
//        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        var input = new ProductInputTo("Product Name", 10.0, 8);
//
//        var output = this.productServiceImpl.save(input);
//
//        Assertions.assertThat(product)
//                .usingRecursiveComparison()
//                .isEqualTo(output);
//    }

}
