package com.tours.tour_service.service;

import com.tours.tour_service.DTO.ReviewDTO;
import com.tours.tour_service.model.Review;
import com.tours.tour_service.repo.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final String uploadDirectory = "/app/uploads/";

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public Review addReview(ReviewDTO dto, MultipartFile[] files) throws IOException {
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        LocalDate visitDate = LocalDate.parse(dto.getVisitDate());
        if (visitDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Visit date cannot be in the future.");
        }

        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setTouristId(dto.getTouristId());
        review.setTourId(dto.getTourId());
        review.setVisitDate(visitDate);
        review.setCommentDate(LocalDate.now());

        List<String> savedUrls = new ArrayList<>();
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path targetPath = Paths.get(uploadDirectory + uniqueFileName);
                    Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    savedUrls.add("http://localhost:8083/uploads/" + uniqueFileName);
                }
            }
        }

        review.setImageUrls(savedUrls);
        return repository.save(review);
    }

    public List<Review> getReviewsByTour(String tourId) {
        return repository.findByTourId(tourId);
    }
}