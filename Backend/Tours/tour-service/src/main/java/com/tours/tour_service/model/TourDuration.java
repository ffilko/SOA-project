package com.tours.tour_service.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import com.tours.tour_service.enums.TransportType;

@Node("TourDuration")
public class TourDuration {

    @Id
    private String id = UUID.randomUUID().toString();

    private TransportType transportType;
    private int duration;

    public TourDuration() {}

    public TourDuration(TransportType transportType, int duration) {
        this.transportType = transportType;
        this.duration = duration;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(TransportType transportType) {
		this.transportType = transportType;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

    
}
	