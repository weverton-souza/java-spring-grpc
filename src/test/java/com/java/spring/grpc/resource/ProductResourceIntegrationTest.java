package com.java.spring.grpc.resource;

import com.java.spring.grpc.ProductReq;
import com.java.spring.grpc.ProductRes;
import com.java.spring.grpc.ProductServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
public class ProductResourceIntegrationTest {

    @GrpcClient("in-process")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @Test
    @DisplayName("Create a Product Successfully")
    public void createProductSuccessfullTest() {
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


}
