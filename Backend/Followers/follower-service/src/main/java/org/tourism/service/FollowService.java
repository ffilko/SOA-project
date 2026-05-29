package org.tourism.service;

import org.tourism.model.UserNode;
import org.springframework.stereotype.Service;
import org.tourism.repo.UserRepository;

import java.util.List;

@Service
public class FollowService {

	private final UserRepository userRepository;

	public FollowService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void follow(String followerId, String followingId) {

		UserNode follower = userRepository.findById(followerId).orElse(new UserNode(followerId));
		UserNode following = userRepository.findById(followingId).orElse(new UserNode(followingId));

		follower.getFollowing().add(following);
		userRepository.save(follower);
	}

	public void unfollow(String followerId, String followingId) {
		userRepository.unfollow(followerId, followingId);
	}

	public List<String> getFollowing(String userId) {
		return userRepository.findFollowingIds(userId);
	}

	public List<String> getRecommendations(String userId) {
		return userRepository.getRecommendations(userId);
	}

	public boolean isFollowing(String followerId, String followingId) {
		return userRepository.isFollowing(followerId, followingId);
	}

	public void deleteAllByUser(String userId) {
		userRepository.removeAllFollowsByUser(userId);
		userRepository.removeAllFollowersOfUser(userId);
		userRepository.deleteWithAllRelationships(userId);
	}

	public void createUserNode(String userId) {
		if (userRepository.existsById(userId)) {
			return;
		}

		UserNode node = new UserNode(userId);
		userRepository.save(node);
	}

}
