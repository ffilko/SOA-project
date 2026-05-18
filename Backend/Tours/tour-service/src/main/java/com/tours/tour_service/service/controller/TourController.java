package com.tours.tour_service.service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


import com.tours.tour_service.DTO.TourDTO;
import com.tours.tour_service.model.KeyPoint;
import com.tours.tour_service.model.Tour;
import com.tours.tour_service.service.TourService;


@RestController
@RequestMapping("/api/tours")
public class TourController {
	private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }
    
    @GetMapping("/author/{authorId}")
    public List<Tour> getToursByAuthor(@PathVariable String authorId) {
        return tourService.getToursByAuthorId(authorId);
    }

    @PostMapping
    public Tour createTour(@RequestBody TourDTO dto) {
        return tourService.createTour(dto);
    }
    
    @PostMapping("/{tourId}/keypoints")
    public Tour addKeyPoint(
            @PathVariable String tourId,
            @RequestBody KeyPoint keyPoint
    ) {
        return tourService.addKeyPointToTour(tourId, keyPoint);
    }

    @GetMapping
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }
}
