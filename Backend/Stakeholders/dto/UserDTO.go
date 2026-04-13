package dto

import (
	"github.com/google/uuid"
	"stakeholders.xws.com/model"
)

type UserResponse struct {
	ID        uuid.UUID      `json:"id"`
	Username  string         `json:"username"`
	Email     string         `json:"email"`
	Role      model.UserRole `json:"role"`
	IsBlocked bool           `json:"isBlocked"`
}
