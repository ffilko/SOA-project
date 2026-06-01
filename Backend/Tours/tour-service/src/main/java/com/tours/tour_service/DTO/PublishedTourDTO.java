package com.tours.tour_service.DTO;

import com.tours.tour_service.model.KeyPoint;
import com.tours.tour_service.model.Review;
import com.tours.tour_service.model.TourDuration;
import java.util.List;

public class PublishedTourDTO {
    private String id;
    private String name;
    private String description;
    private double price;
    private double distanceInKm;
    private List<TourDuration> durations;
    private List<KeyPoint> keyPoints;
    private List<Review> reviews;

    public PublishedTourDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getDistanceInKm() { return distanceInKm; }
    public void setDistanceInKm(double distanceInKm) { this.distanceInKm = distanceInKm; }
    public List<TourDuration> getDurations() { return durations; }
    public void setDurations(List<TourDuration> durations) { this.durations = durations; }
    public List<KeyPoint> getKeyPoints() { return keyPoints; }
    public void setKeyPoints(List<KeyPoint> keyPoints) { this.keyPoints = keyPoints; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
}