package org.tourism.controller;

import org.springframework.web.bind.annotation.*;
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
}
