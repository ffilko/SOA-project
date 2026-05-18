package com.tours.tour_service.repo;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.tours.tour_service.model.Tour;

public interface TourRepository extends Neo4jRepository<Tour, String> {
	List<Tour> findByAuthorId(String authorId);
}