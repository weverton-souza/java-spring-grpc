package com.java.spring.grpc.resource;

import com.java.spring.grpc.ProductReq;
import com.java.spring.grpc.ProductRes;
import com.java.spring.grpc.ProductServiceGrpc;
import com.java.spring.grpc.service.ProductService;
import com.java.spring.grpc.to.ProductInputTo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void save(ProductReq req, StreamObserver<ProductRes> responseObserver) {
        var input = new ProductInputTo(req.getName(), req.getPrice(), req.getQuantity());

        var output = this.productService.save(input);

        var res = ProductRes.newBuilder()
                .setId(output.getId())
                .setName(output.getName())
                .setPrice(output.getPrice())
                .setQuantity(output.getQuantity())
                .build();

        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(ProductReq request, StreamObserver<ProductRes> responseObserver) {
        super.findById(request, responseObserver);
    }

    @Override
    public void findAll(ProductReq request, StreamObserver<ProductRes> responseObserver) {
        super.findAll(request, responseObserver);
    }

    @Override
    public void delete(ProductReq request, StreamObserver<ProductRes> responseObserver) {
        super.delete(request, responseObserver);
    }
}
