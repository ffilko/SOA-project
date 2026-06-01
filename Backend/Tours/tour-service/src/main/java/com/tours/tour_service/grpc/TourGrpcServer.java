package com.tours.tour_service.grpc;

import com.tours.tour_service.model.Tour;
import com.tours.tour_service.service.TourService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TourGrpcServer extends TourGrpcServiceGrpc.TourGrpcServiceImplBase {

    private final TourService tourService;

    public TourGrpcServer(TourService tourService) {
        this.tourService = tourService;
    }

    @Override
    public void getTourForPurchase(
            GetTourRequest request,
            StreamObserver<TourPurchaseResponse> responseObserver) {

        Tour tour = tourService.getTourForPurchase(request.getTourId());

        TourPurchaseResponse response = TourPurchaseResponse.newBuilder()
                .setId(tour.getId())
                .setName(tour.getName())
                .setPrice(tour.getPrice())
                .setStatus(tour.getStatus().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}