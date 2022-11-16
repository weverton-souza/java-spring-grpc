package com.java.spring.grpc.service.impl;

import com.java.spring.grpc.domain.Product;
import com.java.spring.grpc.exception.AlreadyExistsException;
import com.java.spring.grpc.exception.NotFoundException;
import com.java.spring.grpc.repository.ProductRepository;
import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    @DisplayName("Create a new product")
    void createNewProductSuccessTest() {
        var product = new Product(1L, "Product Name", 10.0, 8);

        when(productRepository.save(any())).thenReturn(product);

        var input = new ProductInputTo("Product Name", 10.0, 8);

        var output = this.productServiceImpl.save(input);

        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(output);
    }

    @Test
    @DisplayName("Find a product by Id")
    void findProductByIdProductSuccessTest() {
        var product = new Product(1L, "Product Name", 10.0, 8);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var output = this.productServiceImpl.findById(1L);

        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(output);
    }

    @Test
    @DisplayName("AlreadyExistsException when product with duplicate name.")
    void createNewProductAlreadyExistsExceptionTest() {
        var product = new Product(1L, "Product Name", 10.0, 8);

        when(productRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(product));

        var input = new ProductInputTo("Product Name", 10.0, 8);

        assertThatExceptionOfType(AlreadyExistsException.class)
                .isThrownBy(() -> this.productServiceImpl.save(input));
    }

    @Test
    @DisplayName("NotFoundException when product don't exist on database.")
    void findProductByIdProductExceptionTest() {
        final long ID = 1L;

        when(this.productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> this.productServiceImpl.findById(ID));
    }

    @Test
    @DisplayName("Delete a product by Id.")
    void deleteProductByIdSuccessTest() {
        final long ID = 1L;
        var product = new Product(1L, "Product Name", 10.0, 8);

        when(this.productRepository.findById(any())).thenReturn(Optional.of(product));

        assertThatNoException().isThrownBy(() -> this.productServiceImpl.findById(ID));
    }

    @Test
    @DisplayName("NotFoundException when product don't exist on database.")
    void deleteProductByIdExceptionTest() {
        final long ID = 1L;

        when(this.productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> this.productServiceImpl.delete(ID));
    }

    @Test
    @DisplayName("Find all products.")
    void findAllProductProductSuccessTest() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product A", 10.00, 8),
                new Product(2L, "Product B", 89.40, 2),
                new Product(3L, "Product C", 56.9, 36)
        );

        when(this.productRepository.findAll()).thenReturn(products);

        List<ProductOutputTo> outputs = this.productServiceImpl.findAll();

        assertThat(outputs)
                .extracting("id", "name", "price", "quantity")
                .hasSize(3)
                .contains(
                        tuple(1L, "Product A", 10.00, 8),
                        tuple(3L, "Product C", 56.9, 36),
                        tuple(2L, "Product B", 89.40, 2)
                );
    }

}
