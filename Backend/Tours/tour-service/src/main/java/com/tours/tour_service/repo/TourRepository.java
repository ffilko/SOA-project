package com.tours.tour_service.repo;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.tours.tour_service.model.Tour;

public interface TourRepository extends Neo4jRepository<Tour, String> {
}