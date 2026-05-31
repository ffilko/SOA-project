package com.tours.tour_service.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.tours.tour_service.enums.TourDifficulty;
import com.tours.tour_service.enums.TourStatus;
import com.tours.tour_service.enums.TransportType;


@Node("Tour")
public class Tour {

    @Id
    private String id = UUID.randomUUID().toString();

    private String authorId;
    private String name;
    private String description;
    private TourDifficulty difficulty;
    private List<String> tags;
    private double price;
    private TourStatus status;
    private List<TourDuration> durations;
    private double distanceInKm; 
    private LocalDateTime publishedAt;
    
    @Relationship(type = "HAS_KEYPOINT")
    private List<KeyPoint> keyPoints = new ArrayList<>();
        
	public Tour() {
		super();
	}
	
	public Tour(String id, String authorId, String name, String description, TourDifficulty difficulty ,List<String> tags,
			double price, TourStatus status, List<TourDuration> durations, double distanceInKm, LocalDateTime publishedAt) {
		super();
		this.id = id;
		this.authorId = authorId;
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.tags = tags;
		this.price = price;
		this.status = status;
		this.durations=durations;
		this.distanceInKm=distanceInKm;
		this.publishedAt=publishedAt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public TourDifficulty getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(TourDifficulty difficulty) {
		this.difficulty = difficulty;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public TourStatus getStatus() {
		return status;
	}
	public void setStatus(TourStatus status) {
		this.status = status;
	}

	public List<KeyPoint> getKeyPoints() {
		return keyPoints;
	}

	public void setKeyPoints(List<KeyPoint> keyPoints) {
		this.keyPoints = keyPoints;
	}

	public List<TourDuration> getDurations() {
		return durations;
	}

	public void setDurations(List<TourDuration> durations) {
		this.durations = durations;
	}

	public double getDistanceInKm() {
		return distanceInKm;
	}

	public void setDistanceInKm(double distanceInKm) {
		this.distanceInKm = distanceInKm;
	}

	public LocalDateTime getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(LocalDateTime publishedAt) {
		this.publishedAt = publishedAt;
	}	
		
    
}