package com.tours.tour_service.repo;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.tours.tour_service.model.KeyPoint;
import com.tours.tour_service.model.Tour;

public interface KeyPointRepository extends Neo4jRepository<KeyPoint, String>  {
}
