package grpc

import (
	"context"

	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"stakeholders.xws.com/model"
	"stakeholders.xws.com/proto"
	"stakeholders.xws.com/service"
)

type UserGRPCServer struct {
	proto.UnimplementedUserServiceServer
	UserService *service.UserService
}

func (s *UserGRPCServer) Login(ctx context.Context, req *proto.LoginRequest) (*proto.LoginResponse, error) {
	user, err := s.UserService.Login(req.Username, req.Password)
	if err != nil {
		return nil, status.Error(codes.Unauthenticated, err.Error())
	}

	token, _ := service.GenerateJWT(*user)

	return &proto.LoginResponse{
		Token: token,
	}, nil
}

func (s *UserGRPCServer) Register(ctx context.Context, req *proto.RegisterRequest) (*proto.RegisterResponse, error) {
	user := &model.User{
		Username: req.Username,
		Email:    req.Email,
		Password: req.Password,
	}

	err := s.UserService.Create(user)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return &proto.RegisterResponse{
		Id: user.ID.String(),
	}, nil
}

func (s *UserGRPCServer) GetAllUsers(ctx context.Context, req *proto.EmptyRequest) (*proto.UsersResponse, error) {
	users, err := s.UserService.GetAllUsers()
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	var protoUsers []*proto.UserDetails
	for _, u := range users {
		protoUsers = append(protoUsers, &proto.UserDetails{
			Id:        u.ID.String(),
			Username:  u.Username,
			Email:     u.Email,
			IsBlocked: u.IsBlocked,
		})
	}

	return &proto.UsersResponse{Users: protoUsers}, nil
}

func (s *UserGRPCServer) BlockUser(ctx context.Context, req *proto.BlockUserRequest) (*proto.BlockUserResponse, error) {
	id, err := uuid.Parse(req.Id)
	if err != nil {
		return nil, status.Error(codes.InvalidArgument, "invalid UUID")
	}

	err = s.UserService.BlockUser(id)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return &proto.BlockUserResponse{
		Success: true,
		Message: "User blocked successfully",
	}, nil
}
