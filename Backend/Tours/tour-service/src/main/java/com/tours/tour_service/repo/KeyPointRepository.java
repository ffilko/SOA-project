package com.tours.tour_service.repo;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.tours.tour_service.model.KeyPoint;

public interface KeyPointRepository extends Neo4jRepository<KeyPoint, String>  {

}
