package com.java.spring.grpc.resource;

import com.java.spring.grpc.ProductReq;
import com.java.spring.grpc.ProductRes;
import com.java.spring.grpc.ProductServiceGrpc;
import com.java.spring.grpc.RequestById;
import com.java.spring.grpc.service.ProductService;
import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
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

        this.onFinally(responseObserver, this.buidResponse(output));
    }

    @Override
    public void findById(RequestById request, StreamObserver<ProductRes> responseObserver) {
        var output = this.productService.findById(request.getId());

        this.onFinally(responseObserver, this.buidResponse(output));
    }

    private ProductRes buidResponse(ProductOutputTo output) {
        return ProductRes.newBuilder()
                .setId(output.getId())
                .setName(output.getName())
                .setPrice(output.getPrice())
                .setQuantity(output.getQuantity())
                .build();
    }

    private void onFinally(StreamObserver<ProductRes> responseObserver, ProductRes res) {
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

}
