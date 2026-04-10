package repo

import (
	"github.com/google/uuid"
	"gorm.io/gorm"
	"stakeholders.xws.com/model"
)

type ProfileRepository struct {
	DatabaseConnection *gorm.DB
}

func (repo *ProfileRepository) CreateProfile(profile *model.Profile) error {
	dbResult := repo.DatabaseConnection.Create(profile)
	return dbResult.Error
}

func (repo *ProfileRepository) FindByUserID(userID uuid.UUID) (model.Profile, error) {
	profile := model.Profile{}
	dbResult := repo.DatabaseConnection.First(&profile, "user_id = ?", userID)
	return profile, dbResult.Error
}
