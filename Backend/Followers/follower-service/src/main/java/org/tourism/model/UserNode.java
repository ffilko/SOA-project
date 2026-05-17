package org.tourism.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("User")
public class UserNode {

	@Id
	private String userId;

	@Relationship(type = "FOLLOWS")
	private Set<UserNode> following = new HashSet<>();

	public UserNode() {}

	public UserNode(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public Set<UserNode> getFollowing() {
		return following;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFollowing(Set<UserNode> following) {
		this.following = following;
	}
}
