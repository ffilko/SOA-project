package org.tourism.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourism.dto.UserRegisteredEventDTO;
import org.tourism.service.FollowService;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

	private final FollowService followService;

	public FollowController(FollowService followService) {
		this.followService = followService;
	}

	@PostMapping("/{followerId}/{followingId}")
	public void follow(@PathVariable String followerId, @PathVariable String followingId) {
		followService.follow(followerId, followingId);
	}

	@DeleteMapping("/{followerId}/{followingId}")
	public void unfollow(@PathVariable String followerId, @PathVariable String followingId) {
		followService.unfollow(followerId, followingId);
	}

	@GetMapping("/{userId}/following")
	public List<String> getFollowing(@PathVariable String userId) {
		return followService.getFollowing(userId);
	}

	@GetMapping("/{userId}/recommendations")
	public List<String> getRecommendations(@PathVariable String userId) {
		return followService.getRecommendations(userId);
	}

	@GetMapping("/check/{followerId}/{followingId}")
	public boolean isFollowing(@PathVariable String followerId, @PathVariable String followingId) {
		return followService.isFollowing(followerId, followingId);
	}

	@DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAllByUser(@PathVariable String userId) {
        followService.deleteAllByUser(userId);
        return ResponseEntity.noContent().build();
    }

	@PostMapping("/user")
	public ResponseEntity<?> handleUserRegistered(@RequestBody UserRegisteredEventDTO dto) {
		try {
			followService.createUserNode(dto.getUserId());
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Neo4j čvor kreiran za korisnika: " + dto.getUserId());
		} catch (Exception e) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Greška pri kreiranju čvora: " + e.getMessage());
		}
	}
}
