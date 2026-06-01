package com.tours.tour_service.DTO;

import java.util.List;

import com.tours.tour_service.enums.TourDifficulty;
import com.tours.tour_service.model.KeyPoint;
import com.tours.tour_service.model.TourDuration;

public class TourDTO {
	private String authorId;
    private String name;
    private String description;
    private TourDifficulty difficulty;
    private List<String> tags;
    private List<KeyPoint> keyPoints;
    private List<TourDuration> durations;
    
	public TourDTO() {
		super();
	}
	
	public TourDTO(String authorId, String name, String description, TourDifficulty difficulty, List<String> tags, List<KeyPoint> keyPoints, List<TourDuration> durations) {
		super();
		this.authorId = authorId;
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.tags = tags;
		this.keyPoints=keyPoints;
		this.durations=durations;
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
	
    
    

}
