package com.java.spring.grpc.resource;

import com.java.spring.grpc.ProductReq;
import com.java.spring.grpc.ProductRes;
import com.java.spring.grpc.ProductServiceGrpc;
import io.grpc.StatusRuntimeException;
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

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
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

        Assertions
                .assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.productServiceBlockingStub.save(productReq))
                .withMessage("ALREADY_EXISTS: Item 'Product A' já está cadastrado no sistema.");
    }
}
