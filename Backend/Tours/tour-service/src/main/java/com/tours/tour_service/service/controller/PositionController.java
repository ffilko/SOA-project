package com.tours.tour_service.service.controller;

import com.tours.tour_service.DTO.PositionDTO;
import com.tours.tour_service.model.Position;
import com.tours.tour_service.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin(origins = "http://localhost:4200")
public class PositionController {

    private final PositionService service;

    public PositionController(PositionService service) {
        this.service = service;
    }

    @PutMapping
    public ResponseEntity<Position> upsert(@RequestBody PositionDTO dto) {
        return new ResponseEntity<>(service.upsert(dto), HttpStatus.OK);
    }

    @GetMapping("/{touristId}")
    public ResponseEntity<Position> getByTourist(@PathVariable String touristId) {
        return service.getByTourist(touristId)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}