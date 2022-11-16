package com.java.spring.grpc.resource;

import com.java.spring.grpc.EmptyRequest;
import com.java.spring.grpc.ProductReq;
import com.java.spring.grpc.ProductRes;
import com.java.spring.grpc.ProductResponseList;
import com.java.spring.grpc.ProductServiceGrpc;
import com.java.spring.grpc.RequestById;
import io.grpc.StatusRuntimeException;
import java.math.BigDecimal;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@SuppressWarnings({"ResultOfMethodCallIgnored"})
@TestPropertySource("classpath:application-test.yml")
class ProductResourceIntegrationTest {

    @GrpcClient("in-process")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;
    private final Flyway flyway;

    public ProductResourceIntegrationTest(ApplicationContext context) {
        this.flyway = (Flyway) context.getBean("flyway");
    }

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @DisplayName("Create a Product Successfully")
    void createProductSuccessfullTest() {
        ProductReq productReq = ProductReq.newBuilder()
                .setName("Coca-Cola")
                .setPrice(7.9)
                .setQuantity(14)
                .build();

        ProductRes productRes = productServiceBlockingStub.save(productReq);

        Assertions.assertThat(productReq)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantity")
                .isEqualTo(productRes);
    }

    @Test
    @DisplayName("AlreadyExistsException when product with duplicate name.")
    void createProductAlreadyExistsExceptionTest() {
        ProductReq productReq = ProductReq.newBuilder()
                .setName("Product A")
                .setPrice(7.9)
                .setQuantity(14)
                .build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.productServiceBlockingStub.save(productReq))
                .withMessage("ALREADY_EXISTS: Item 'Product A' já está cadastrado no sistema.");
    }

    @Test
    @DisplayName("NotFoundException when product don't exist on database.")
    void createProductNotFoundExceptionTest() {
        final long ID = new BigDecimal("1e6").longValue();
        RequestById productReq = RequestById.newBuilder()
                .setId(ID)
                .build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.productServiceBlockingStub.findById(productReq))
                .withMessage(String.format("NOT_FOUND: Item com ID '%d' não encontrado.", ID));
    }

    @Test
    @DisplayName("Delete a product by Id.")
    void deleteProductByIdSuccessTest() {
        var productReq = RequestById.newBuilder()
                .setId(1L)
                .build();

        assertThatNoException().isThrownBy(() -> this.productServiceBlockingStub.delete(productReq));
    }

    @Test
    @DisplayName("NotFoundException when trying to delete a product.")
    void deleteProductByIdExceptionTest() {
        final long ID = new BigDecimal("1e6").longValue();
        var productReq = RequestById.newBuilder()
                .setId(ID)
                .build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.productServiceBlockingStub.findById(productReq))
                .withMessage(String.format("NOT_FOUND: Item com ID '%d' não encontrado.", ID));
    }

    @Test
    @DisplayName("NotFoundException when trying to delete a product.")
    void findAllProducsExceptionTest() {
        EmptyRequest request = EmptyRequest.newBuilder().build();

        ProductResponseList responseList = this.productServiceBlockingStub.findAll(request);

        assertThat(responseList).isInstanceOf(ProductResponseList.class);
        assertThat(responseList.getProductsCount()).isEqualTo(2);
        assertThat(responseList.getProductsList())
                .extracting("id", "name", "price", "quantity")
                .contains(
                        tuple(1L, "Product A", 10.99, 10),
                        tuple(2L, "Product B", 10.99, 10)
                );
    }

}
