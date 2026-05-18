package com.tours.tour_service.service;

import com.tours.tour_service.DTO.PositionDTO;
import com.tours.tour_service.model.Position;
import com.tours.tour_service.repo.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PositionService {

    private final PositionRepository repository;

    public PositionService(PositionRepository repository) {
        this.repository = repository;
    }

    public Position upsert(PositionDTO dto) {
        Optional<Position> existing = repository.findByTouristId(dto.getTouristId());

        Position position = existing.orElse(new Position());
        position.setTouristId(dto.getTouristId());
        position.setLatitude(dto.getLatitude());
        position.setLongitude(dto.getLongitude());

        return repository.save(position);
    }

    public Optional<Position> getByTourist(String touristId) {
        return repository.findByTouristId(touristId);
    }
}