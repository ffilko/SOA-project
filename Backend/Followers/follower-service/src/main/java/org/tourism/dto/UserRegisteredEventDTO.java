package org.tourism.dto;

public class UserRegisteredEventDTO {
	private String userId;

	public UserRegisteredEventDTO() {}

	public UserRegisteredEventDTO(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
