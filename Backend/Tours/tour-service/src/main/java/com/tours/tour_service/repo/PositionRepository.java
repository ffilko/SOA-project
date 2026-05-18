package com.tours.tour_service.repo;

import com.tours.tour_service.model.Position;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.Optional;

public interface PositionRepository extends Neo4jRepository<Position, String> {
    Optional<Position> findByTouristId(String touristId);
}