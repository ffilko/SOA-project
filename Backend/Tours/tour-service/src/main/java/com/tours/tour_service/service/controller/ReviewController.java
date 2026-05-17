package com.tours.tour_service.controller;

import com.tours.tour_service.DTO.ReviewDTO;
import com.tours.tour_service.model.Review;
import com.tours.tour_service.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Review> createReview(
            @ModelAttribute ReviewDTO dto,
            @RequestParam(value = "files", required = false) MultipartFile[] files) {
        try {
            Review savedReview = service.addReview(dto, files);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<Review>> getReviewsByTour(@PathVariable String tourId) {
        return new ResponseEntity<>(service.getReviewsByTour(tourId), HttpStatus.OK);
    }
}