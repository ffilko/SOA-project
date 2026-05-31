package com.tours.tour_service.model;

import com.tours.tour_service.enums.TransportType;

public class TourDuration {
	
	private TransportType transportType;
    private int duration;
    
	public TourDuration(TransportType transportType, int duration) {
		super();
		this.transportType = transportType;
		this.duration = duration;
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
