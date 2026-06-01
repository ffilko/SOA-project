package com.tours.tour_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tours.tour_service.DTO.TourDTO;
import com.tours.tour_service.enums.TourStatus;
import com.tours.tour_service.enums.TransportType;
import com.tours.tour_service.model.KeyPoint;
import com.tours.tour_service.model.Tour;
import com.tours.tour_service.model.TourDuration;
import com.tours.tour_service.repo.KeyPointRepository;
import com.tours.tour_service.repo.TourRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TourService {
	
	private final TourRepository tourRepository;
	private final KeyPointRepository keyPointRepository;

	public TourService(TourRepository tourRepository, KeyPointRepository keyPointRepository) {
	    this.tourRepository = tourRepository;
	    this.keyPointRepository=keyPointRepository;
	}
	

	public List<Tour> getAllTours() {
		return tourRepository.findAll();
	}
	
	public List<Tour> getToursByAuthorId(String authorId) {
	    return tourRepository.findByAuthorId(authorId);
	}
	
	public Tour createTour(TourDTO dto) {

	    if (dto.getKeyPoints() == null || dto.getKeyPoints().size() < 1) {
	        throw new RuntimeException("Tour must have at least one key point");
	    }

	    Tour tour = new Tour();

	    tour.setAuthorId(dto.getAuthorId());
	    tour.setName(dto.getName());
	    tour.setDescription(dto.getDescription());
	    tour.setDifficulty(dto.getDifficulty());
	    tour.setTags(dto.getTags());

	    tour.setStatus(TourStatus.DRAFT);
	    tour.setPrice(0);
	    tour.setPublishedAt(null);
	    tour.setArchivedAt(null);

	    List<KeyPoint> keyPoints = new ArrayList<>();

	    for (KeyPoint kp : dto.getKeyPoints()) {
	        KeyPoint newKp = new KeyPoint();
	        newKp.setId(UUID.randomUUID().toString());
	        newKp.setName(kp.getName());
	        newKp.setDescription(kp.getDescription());
	        newKp.setLatitude(kp.getLatitude());
	        newKp.setLongitude(kp.getLongitude());
	        newKp.setImageUrl(kp.getImageUrl());

	        keyPoints.add(newKp);
	    }

	    tour.setKeyPoints(keyPoints);

	    if (dto.getDurations() != null) {
	        tour.setDurations(dto.getDurations());
	    } else {
	        tour.setDurations(new ArrayList<>());
	    }

	    double distance = calculateTourDistance(keyPoints);
	    tour.setDistanceInKm(distance);

	    return tourRepository.save(tour);
	}
	
	public Tour addKeyPointToTour(String tourId, KeyPoint keyPoint) {
	    Tour tour = tourRepository.findById(tourId)
	            .orElseThrow(() -> new RuntimeException("Tour not found"));

	    keyPoint.setId(UUID.randomUUID().toString());

	    KeyPoint savedKeyPoint = keyPointRepository.save(keyPoint);

	    if (tour.getKeyPoints() == null) {
	        tour.setKeyPoints(new ArrayList<>());
	    }

	    tour.getKeyPoints().add(savedKeyPoint);

	    if (tour.getKeyPoints().size() >= 2) {
	        tour.setDistanceInKm(calculateTourDistance(tour.getKeyPoints()));
	    }

	    return tourRepository.save(tour);
	}
	
	public List<KeyPoint> getKeyPoints(String tourId) {
	    Tour tour = tourRepository.findById(tourId)
	            .orElseThrow(() -> new RuntimeException("Tour not found"));

	    return tour.getKeyPoints();
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

	    if (tour.getName() == null || tour.getName().isBlank()
	            || tour.getDescription() == null || tour.getDescription().isBlank()
	            || tour.getDifficulty() == null
	            || tour.getTags() == null || tour.getTags().isEmpty()) {
	        throw new RuntimeException("Tour must contain basic information");
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
	
	public Tour archiveTour(String tourId) {
	    Tour tour = tourRepository.findById(tourId)
	            .orElseThrow(() -> new RuntimeException("Tour not found"));

	    
	    tour.setStatus(TourStatus.ARCHIVED);
	    tour.setArchivedAt(LocalDateTime.now());

	    return tourRepository.save(tour);
	}
	
	public Tour addDuration(String tourId, TourDuration duration) {
	    Tour tour = tourRepository.findById(tourId)
	            .orElseThrow(() -> new RuntimeException("Tour not found"));

	    if (tour.getDurations() == null) {
	        tour.setDurations(new ArrayList<>());
	    }

	    duration.setId(UUID.randomUUID().toString());

	    tour.getDurations().add(duration);

	    return tourRepository.save(tour);
	}
	
	public Tour getTourForPurchase(String tourId) {
	    Tour tour = tourRepository.findById(tourId)
	            .orElseThrow(() -> new RuntimeException("Tour not found"));

	    if (tour.getStatus() != TourStatus.PUBLISHED) {
	        throw new RuntimeException("Only published tours can be bought");
	    }

	    return tour;
	}
}
