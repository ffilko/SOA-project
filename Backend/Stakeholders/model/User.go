package model

import (
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type User struct {
	ID       uuid.UUID `gorm:"type:uuid;primary_key;" json:"id"`
	Username string    `gorm:"not null;unique" json:"username"`
	Password string    `gorm:"not null" json:"password"`
	Email    string    `gorm:"not null;unique" json:"email"`
	Role     UserRole  `gorm:"not null" json:"role"`
}

// Generisanje ID
func (user *User) BeforeCreate(db *gorm.DB) error {
	user.ID = uuid.New()
	return nil
}
