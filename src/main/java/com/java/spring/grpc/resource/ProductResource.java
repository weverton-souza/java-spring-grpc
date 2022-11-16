package com.java.spring.grpc.resource;

import com.java.spring.grpc.EmptyRequest;
import com.java.spring.grpc.EmptyResponse;
import com.java.spring.grpc.ProductReq;
import com.java.spring.grpc.ProductRes;
import com.java.spring.grpc.ProductResponseList;
import com.java.spring.grpc.ProductServiceGrpc;
import com.java.spring.grpc.RequestById;
import com.java.spring.grpc.service.ProductService;
import com.java.spring.grpc.to.ProductInputTo;
import com.java.spring.grpc.to.ProductOutputTo;
import io.grpc.stub.StreamObserver;
import java.util.List;
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

        onFinally(responseObserver, this.buidResponse(output));
    }

    @Override
    public void findById(RequestById request, StreamObserver<ProductRes> responseObserver) {
        var output = this.productService.findById(request.getId());

        onFinally(responseObserver, this.buidResponse(output));
    }

    @Override
    public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
        var outputList = this.productService.findAll();

        onFinally(responseObserver, this.buidResponse(outputList));
    }

    @Override
    public void delete(RequestById request, StreamObserver<EmptyResponse> responseObserver) {
        this.productService.delete(request.getId());

        onFinally(responseObserver, EmptyResponse.newBuilder().build());
    }

    private ProductRes buidResponse(ProductOutputTo output) {
        return ProductRes.newBuilder()
                .setId(output.getId())
                .setName(output.getName())
                .setPrice(output.getPrice())
                .setQuantity(output.getQuantity())
                .build();
    }

    private ProductResponseList buidResponse(List<ProductOutputTo> outputList) {
        var resList = ProductResponseList.newBuilder();
        outputList.forEach(output -> resList.addProducts(ProductRes.newBuilder()
                .setId(output.getId())
                .setName(output.getName())
                .setPrice(output.getPrice())
                .setQuantity(output.getQuantity()).build()));

        return resList.build();
    }

    public static <E> void onFinally(StreamObserver<E> observer, E res) {
        observer.onNext(res);
        observer.onCompleted();
    }

}
