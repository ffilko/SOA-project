package service

import (
	"fmt"

	"github.com/google/uuid"
	"stakeholders.xws.com/model"
	"stakeholders.xws.com/repo"
)

type ProfileService struct {
	ProfileRepo *repo.ProfileRepository
}

func (service *ProfileService) FindUser(id uuid.UUID) (*model.Profile, error) {
	profile, err := service.ProfileRepo.FindByUserID(id)
	if err != nil {
		return nil, fmt.Errorf("Profile with id %d not found", id)
	}
	return &profile, nil
}
