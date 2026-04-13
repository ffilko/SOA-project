package service

import (
	"fmt"

	"github.com/google/uuid"
	"stakeholders.xws.com/dto"
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

func (service *ProfileService) Update(id uuid.UUID, updatedDTO dto.UpdateProfileDTO) (*model.Profile, error) {
	profile, err := service.ProfileRepo.FindByUserID(id)
	if err != nil {
		return nil, fmt.Errorf("Profile with id %s not found", id)
	}

	profile.Name = updatedDTO.Name
	profile.Surname = updatedDTO.Surname
	profile.ProfileImage = updatedDTO.ProfileImage
	profile.Description = updatedDTO.Description
	profile.Motto = updatedDTO.Motto

	err = service.ProfileRepo.Update(&profile)
	if err != nil {
		return nil, fmt.Errorf("Error while updating profile")
	}

	return &profile, nil
}
