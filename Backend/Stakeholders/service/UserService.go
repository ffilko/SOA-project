package service

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/codes"
	"go.opentelemetry.io/otel/trace"
	"stakeholders.xws.com/model"
	"stakeholders.xws.com/repo"
	"stakeholders.xws.com/saga"
)

type SagaSavedUser struct {
	ID       string
	Username string
	Email    string
	Password string
	Role     string
}

type UserService struct {
	UserRepo    *repo.UserRepository
	ProfileRepo *repo.ProfileRepository
	Tracer      trace.Tracer
}

func (service *UserService) FindUser(id uuid.UUID) (*model.User, error) {
	user, err := service.UserRepo.FindById(id)
	if err != nil {
		return nil, fmt.Errorf("User with id %d not found", id)
	}
	return &user, nil
}

func (service *UserService) Login(username string, password string) (*model.User, error) {
	_, span := service.Tracer.Start(
		context.Background(),
		"UserService.Login",
	)
	defer func() { span.End() }()

	span.SetAttributes(
		attribute.String("username", username),
	)

	user, err := service.UserRepo.FindByUsername(username)
	if err != nil {
		span.RecordError(err)
		span.SetStatus(codes.Error, "user not found")
		return nil, fmt.Errorf("Invalid credentials")
	}

	if user.IsBlocked {
		err := fmt.Errorf("blocked user")
		span.RecordError(err)
		span.SetStatus(codes.Error, err.Error())
		return nil, err
	}

	if user.Password != password {
		err := fmt.Errorf("wrong password")
		span.RecordError(err)
		span.SetStatus(codes.Error, err.Error())
		return nil, err
	}

	span.SetStatus(codes.Ok, "login success")
	return &user, nil
}

func (service *UserService) Create(user *model.User) error {
	_, span := service.Tracer.Start(context.Background(), "UserService.Create")
	defer func() { span.End() }()

	if user.Role == model.Administrator {
		err := fmt.Errorf("admin not allowed")
		span.RecordError(err)
		return err
	}

	err := service.UserRepo.CreateUser(user)
	if err != nil {
		span.RecordError(err)
		return err
	}

	span.AddEvent("User created")

	profile := &model.Profile{
		UserID: user.ID,
	}
	if err := service.ProfileRepo.CreateProfile(profile); err != nil {
		span.RecordError(err)
		return err
	}

	span.AddEvent("Profile created")
	return nil
}

func (service *UserService) GetAllUsers() ([]model.User, error) {
	return service.UserRepo.GetAllUsers()
}

func (service *UserService) BlockUser(id uuid.UUID) error {
	user, err := service.UserRepo.FindById(id)
	if err != nil {
		return err
	}

	user.IsBlocked = true

	return service.UserRepo.UpdateUser(&user)
}

func (s *UserService) SoftDeleteUser(idStr string) (*saga.SavedUser, error) {
	id, err := uuid.Parse(idStr)
	if err != nil {
		return nil, fmt.Errorf("invalid UUID: %w", err)
	}

	user, err := s.UserRepo.FindById(id)
	if err != nil {
		return nil, err
	}

	snapshot := &saga.SavedUser{
		ID:       user.ID.String(),
		Username: user.Username,
		Email:    user.Email,
		Password: user.Password,
		Role:     model.UserRoleToString(user.Role),
	}

	if err := s.UserRepo.DeleteUser(id); err != nil {
		return nil, err
	}

	return snapshot, nil
}

func (s *UserService) RestoreUser(saved *saga.SavedUser) error {
	id, err := uuid.Parse(saved.ID)
	if err != nil {
		return err
	}
	user := model.User{
		ID:       id,
		Username: saved.Username,
		Email:    saved.Email,
		Password: saved.Password,
		Role:     model.UserRoleFromString(saved.Role),
	}
	return s.UserRepo.CreateUser(&user)
}

func (s *UserService) DeleteUser(id uuid.UUID) error {
	return s.UserRepo.DeleteUser(id)
}
