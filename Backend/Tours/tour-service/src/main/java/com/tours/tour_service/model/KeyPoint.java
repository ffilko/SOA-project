package com.tours.tour_service.model;

import java.util.UUID;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("KeyPoint")
public class KeyPoint {
	
	@Id
	private String id = UUID.randomUUID().toString();
	private String name;
    private String description;
	private double latitude;
	private double longitude;
	private String imageUrl; 
	 

	public KeyPoint() {
		super();
	}
	
	

	public KeyPoint(String id, String name, String description, double latitude, double longitude, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	 
	 

}
