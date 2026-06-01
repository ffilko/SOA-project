package com.tours.tour_service.service;

import java.util.*;

import com.tours.tour_service.DTO.PublishedTourDTO;
import com.tours.tour_service.model.Review;
import org.springframework.stereotype.Service;

import com.tours.tour_service.DTO.TourDTO;
import com.tours.tour_service.enums.TourStatus;
import com.tours.tour_service.enums.TransportType;
import com.tours.tour_service.model.KeyPoint;
import com.tours.tour_service.model.Tour;
import com.tours.tour_service.model.TourDuration;
import com.tours.tour_service.repo.TourRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class TourService {
	
	private final TourRepository tourRepository;
	private final ReviewService reviewService;

	public TourService(TourRepository tourRepository, ReviewService reviewService) {
	    this.tourRepository = tourRepository;
		this.reviewService = reviewService;
	}
	
	public List<Tour> getToursByAuthorId(String authorId) {
	    return tourRepository.findByAuthorId(authorId);
	}
	
	public Tour createTour(TourDTO dto) {

	    Tour tour = new Tour();
	    
	    tour.setAuthorId(dto.getAuthorId());
	    tour.setName(dto.getName());
	    tour.setDescription(dto.getDescription());
	    tour.setDifficulty(dto.getDifficulty());
	    tour.setTags(dto.getTags());

	    tour.setPrice(0);
	    tour.setStatus(TourStatus.DRAFT);
	    tour.setDistanceInKm(0);
	    List<TourDuration> durations = new ArrayList<>();
	    durations.add(new TourDuration(TransportType.BICYCLE, 200));

	    tour.setDurations(durations);
	    tour.setArchivedAt(null);
	    tour.setPublishedAt(null);

	    return tourRepository.save(tour);
	}
	
	public Tour addKeyPointToTour(String tourId, KeyPoint keyPoint) {
	    Tour tour = tourRepository.findById(tourId)
	            .orElseThrow(() -> new RuntimeException("Tour not found"));
	    
	    tour.getKeyPoints().add(keyPoint);
	    
	    if (tour.getKeyPoints().size() >= 2) {
	        double distance = calculateTourDistance(tour.getKeyPoints());
	        tour.setDistanceInKm(distance);
	    }

	    return tourRepository.save(tour);
	}

	public List<Tour> getAllTours() {
		return tourRepository.findAll();
	}
	
	private double calculateTourDistance(List<KeyPoint> keyPoints) {
	    double totalDistance = 0;

	    for (int i = 1; i < keyPoints.size(); i++) {
	        KeyPoint previous = keyPoints.get(i - 1);
	        KeyPoint current = keyPoints.get(i);

	        totalDistance += calculateDistanceBetweenPoints(previous, current);
	    }

	    return totalDistance;
	}

	private double calculateDistanceBetweenPoints(KeyPoint first, KeyPoint second) {
	    final double EARTH_RADIUS = 6371.0;

	    double lat1 = Math.toRadians(first.getLatitude());
	    double lon1 = Math.toRadians(first.getLongitude());

	    double lat2 = Math.toRadians(second.getLatitude());
	    double lon2 = Math.toRadians(second.getLongitude());

	    double dLat = lat2 - lat1;
	    double dLon = lon2 - lon1;

	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
	            + Math.cos(lat1) * Math.cos(lat2)
	            * Math.sin(dLon / 2) * Math.sin(dLon / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return EARTH_RADIUS * c;
	}
	
	public Tour publishTour(String tourId) {
	    Tour tour = tourRepository.findById(tourId)
	            .orElseThrow(() -> new RuntimeException("Tour not found"));

	    if (tour.getStatus() != TourStatus.DRAFT && tour.getStatus() != TourStatus.ARCHIVED) {
	        throw new RuntimeException("Only draft or archived tours can be published");
	    }

	    if (tour.getName() == null || tour.getName().isBlank()) {
	        throw new RuntimeException("Tour must have name");
	    }

	    if (tour.getDescription() == null || tour.getDescription().isBlank()) {
	        throw new RuntimeException("Tour must have description");
	    }

	    if (tour.getDifficulty() == null) {
	        throw new RuntimeException("Tour must have difficulty");
	    }

	    if (tour.getTags() == null || tour.getTags().isEmpty()) {
	        throw new RuntimeException("Tour must have tags");
	    }

	    if (tour.getKeyPoints() == null || tour.getKeyPoints().size() < 2) {
	        throw new RuntimeException("Tour must have at least two key points");
	    }

	    if (tour.getDurations() == null || tour.getDurations().isEmpty()) {
	        throw new RuntimeException("Tour must have at least one duration");
	    }

	    tour.setStatus(TourStatus.PUBLISHED);
	    tour.setPublishedAt(LocalDateTime.now());

	    return tourRepository.save(tour);
	}

	public List<PublishedTourDTO> getPublishedTours() {
		List<Tour> publishedTours = tourRepository.findByStatus(TourStatus.PUBLISHED);

		return publishedTours.stream().map(tour -> {
			PublishedTourDTO dto = new PublishedTourDTO();
			dto.setId(tour.getId());
			dto.setName(tour.getName());
			dto.setDescription(tour.getDescription());
			dto.setPrice(tour.getPrice());
			dto.setDistanceInKm(tour.getDistanceInKm());
			dto.setDurations(tour.getDurations());

			if (tour.getKeyPoints() != null && !tour.getKeyPoints().isEmpty()) {
				dto.setKeyPoints(Collections.singletonList(tour.getKeyPoints().get(0)));
			} else {
				dto.setKeyPoints(Collections.emptyList());
			}

			List<Review> reviews = reviewService.getReviewsByTour(tour.getId());
			dto.setReviews(reviews);

			return dto;
		}).collect(Collectors.toList());
	}

	public PublishedTourDTO getPurchasedTourDetails(String tourId) {
		Tour tour = tourRepository.findById(tourId)
				.orElseThrow(() -> new RuntimeException("Tour not found."));

		PublishedTourDTO dto = new PublishedTourDTO();
		dto.setId(tour.getId());
		dto.setName(tour.getName());
		dto.setDescription(tour.getDescription());
		dto.setPrice(tour.getPrice());
		dto.setDistanceInKm(tour.getDistanceInKm());
		dto.setDurations(tour.getDurations());
		dto.setKeyPoints(tour.getKeyPoints());

		List<Review> reviews = reviewService.getReviewsByTour(tour.getId());
		dto.setReviews(reviews);

		return dto;
	}
}
