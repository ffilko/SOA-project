package grpc

import (
	"context"

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
