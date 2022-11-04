package com.java.spring.grpc.converter;

import com.java.spring.grpc.domain.Product;
import com.java.spring.grpc.to.ProductInputTo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductConverterTest {

    @Test
    @DisplayName("Convert a Product to a ProductOutputTo")
    public void productToProductOutputTo() {
        var product = new Product(1L, "Product Name", 10.0, 8);
        var productOutputTo = ProductConverter.productToProductOutputTo(product);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productOutputTo);
    }

    @Test
    @DisplayName("Convert a ProductInputTo to a Product")
    public void productInputToProduct() {
        var input = new ProductInputTo("Product Name", 10.0, 8);
        var product = ProductConverter.productInputToProduct(input);

        Assertions.assertThat(input)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }
}
