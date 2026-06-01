package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
	"github.com/rs/cors"
	grpc "google.golang.org/grpc"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	grpcHandler "stakeholders.xws.com/grpc"
	"stakeholders.xws.com/handler"
	"stakeholders.xws.com/model"
	"stakeholders.xws.com/proto"
	"stakeholders.xws.com/repo"
	"stakeholders.xws.com/service"
)

func initDB() *gorm.DB {
	dsn := "host=database user=postgres password=root dbname=tourist_app port=5432 sslmode=disable"

	var database *gorm.DB
	var err error

	for i := 0; i < 10; i++ {
		database, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
		if err == nil {
			break
		}

		fmt.Println("Waiting for database...")
		time.Sleep(2 * time.Second)
	}

	if err != nil {
		fmt.Println(err)
		return nil
	}

	database.AutoMigrate(&model.User{}, &model.Profile{})
	return database
}

func startServer(userHandler *handler.UserHandler, profileHandler *handler.ProfileHandler) {
	router := mux.NewRouter()
	handler.RegisterUserRoutes(router, userHandler)
	handler.RegisterProfileRouter(router, profileHandler)

	conn, err := grpc.DialContext(
		context.Background(),
		"localhost:50051",
		grpc.WithInsecure(),
		grpc.WithBlock(),
	)
	if err != nil {
		log.Fatalln("Failed to dial gRPC server:", err)
	}

	gwmux := runtime.NewServeMux()
	err = proto.RegisterUserServiceHandler(context.Background(), gwmux, conn)
	if err != nil {
		log.Fatalln("Failed to register gateway:", err)
	}
	router.PathPrefix("/api/users").Handler(gwmux)

	println("Server starting...")

	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"http://localhost:4200"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Content-Type", "Authorization"},
		AllowCredentials: true,
	})

	handler := c.Handler(router)

	log.Fatal(http.ListenAndServe(":8080", handler))
}

func startGRPC(userService *service.UserService) {
	lis, err := net.Listen("tcp", ":50051")
	if err != nil {
		log.Fatal(err)
	}

	grpcServer := grpc.NewServer()

	proto.RegisterUserServiceServer(grpcServer, &grpcHandler.UserGRPCServer{
		UserService: userService,
	})

	log.Println("gRPC running on :50051")
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatal(err)
	}
}

func main() {
	db := initDB()
	if db == nil {
		fmt.Println("Error with database")
		return
	}

	userRepo := &repo.UserRepository{DatabaseConnection: db}
	profileRepo := &repo.ProfileRepository{DatabaseConnection: db}
	userService := &service.UserService{UserRepo: userRepo, ProfileRepo: profileRepo}
	profileService := &service.ProfileService{ProfileRepo: profileRepo}
	userHandler := &handler.UserHandler{UserService: userService}
	profileHandler := &handler.ProfileHandler{ProfileService: profileService}

	go startGRPC(userService)
	startServer(userHandler, profileHandler)
}
