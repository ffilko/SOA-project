package model

import "github.com/google/uuid"

type Profile struct {
	UserID       uuid.UUID `gorm:"type:uuid;primary_key;" json:"userid"`
	Name         string    `json:"name"`
	Surname      string    `json:"surname"`
	ProfileImage string    `json:"profileimage"`
	Description  string    `json:"description"`
	Motto        string    `json:"motto"`
}
