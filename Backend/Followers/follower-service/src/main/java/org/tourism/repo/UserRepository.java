package org.tourism.repo;

import org.springframework.data.neo4j.repository.query.Query;
import org.tourism.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserRepository extends Neo4jRepository<UserNode, String> {
	@Query("""
		MATCH (a:User)-[r:FOLLOWS]->(b:User)
		WHERE a.userId = $followerId AND b.userId = $followingId
		DELETE r
		""")
	void unfollow(String followerId, String followingId);

	@Query("""
	   MATCH (u:User {userId: $userId})-[:FOLLOWS]->(f)
	   RETURN f.userId AS userId
	   """)
	List<String> findFollowingIds(String userId);

	@Query("""
		MATCH (u:User {userId: $userId})-[:FOLLOWS]->(f1:User)-[:FOLLOWS]->(f2:User)
		WHERE u.userId <> f2.userId
		AND NOT (u)-[:FOLLOWS]->(f2)
		RETURN DISTINCT f2.userId
		""")
	List<String> getRecommendations(String userId);

	@Query("""
		MATCH (a:User {userId: $followerId})-[:FOLLOWS]->(b:User {userId: $followingId})
		RETURN count(b) > 0
		""")
	boolean isFollowing(String followerId, String followingId);
}
