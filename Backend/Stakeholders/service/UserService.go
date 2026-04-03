package service

import (
	"fmt"

	"github.com/google/uuid"
	"stakeholders.xws.com/model"
	"stakeholders.xws.com/repo"
)

type UserService struct {
	UserRepo    *repo.UserRepository
	ProfileRepo *repo.ProfileRepository
}

func (service *UserService) FindUser(id uuid.UUID) (*model.User, error) {
	user, err := service.UserRepo.FindById(id)
	if err != nil {
		return nil, fmt.Errorf("User with id %d not found", id)
	}
	return &user, nil
}

func (service *UserService) Login(username string, password string) (*model.User, error) {
	user, err := service.UserRepo.FindByUsername(username)
	if err != nil {
		return nil, fmt.Errorf("Invalid credentials")
	}

	if user.Password != password {
		return nil, fmt.Errorf("Invalid password")
	}

	return &user, nil
}

func (service *UserService) Create(user *model.User) error {
	if user.Role == model.Administrator {
		return fmt.Errorf("Registration for administrator role is not allowed")
	}

	err := service.UserRepo.CreateUser(user)
	if err != nil {
		return err
	}

	profile := &model.Profile{
		UserID: user.ID,
	}
	return service.ProfileRepo.CreateProfile(profile)
}
