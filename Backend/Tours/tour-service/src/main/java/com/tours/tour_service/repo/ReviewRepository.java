package com.tours.tour_service.repo;

import com.tours.tour_service.model.Review;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;

public interface ReviewRepository extends Neo4jRepository<Review, String> {
    List<Review> findByTourId(String tourId);
}