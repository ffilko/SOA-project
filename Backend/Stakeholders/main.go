package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"stakeholders.xws.com/handler"
	"stakeholders.xws.com/model"
	"stakeholders.xws.com/repo"
	"stakeholders.xws.com/service"
)

func initDB() *gorm.DB {
	connection_url := "host=localhost user=postgres password=root dbname=tourist_app port=5432 sslmode=disable"
	database, err := gorm.Open(postgres.Open(connection_url), &gorm.Config{})
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

	println("Server starting...")
	log.Fatal(http.ListenAndServe(":8080", router))
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

	startServer(userHandler, profileHandler)
}
