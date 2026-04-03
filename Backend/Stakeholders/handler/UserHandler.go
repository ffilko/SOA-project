package handler

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"stakeholders.xws.com/model"
	"stakeholders.xws.com/service"
)

type UserHandler struct {
	UserService *service.UserService
}

func RegisterUserRoutes(router *mux.Router, handler *UserHandler) {
	router.HandleFunc("/user", BlockAuthenticated(handler.Register)).Methods("POST")
	router.HandleFunc("/user/{id}", handler.Get).Methods("GET")
	router.HandleFunc("/login", handler.Login).Methods("POST")
	router.HandleFunc("/logout", handler.Logout).Methods("POST")
}

// Registracija korisnika
func (handler *UserHandler) Register(writer http.ResponseWriter, req *http.Request) {
	var user model.User
	err := json.NewDecoder(req.Body).Decode(&user)

	if err != nil {
		println("Error while parsing json")
		writer.WriteHeader(http.StatusBadRequest)
		return
	}

	err = handler.UserService.Create(&user)
	if err != nil {
		println("Error while creating a new user")
		writer.WriteHeader(http.StatusExpectationFailed)
		return
	}

	writer.WriteHeader(http.StatusCreated)
	writer.Header().Set("Content-Type", "application/json")
}

// Prijava korisnika
func (handler *UserHandler) Login(writer http.ResponseWriter, req *http.Request) {
	var credentials struct {
		Username string `json:"username"`
		Password string `json:"password"`
	}

	err := json.NewDecoder(req.Body).Decode(&credentials)
	if err != nil {
		writer.WriteHeader(http.StatusBadRequest)
		return
	}

	user, err := handler.UserService.Login(credentials.Username, credentials.Password)
	if err != nil {
		writer.WriteHeader(http.StatusUnauthorized)
		json.NewEncoder(writer).Encode(map[string]string{
			"error": "Invalid username or password",
		})
		return
	}

	token, err := service.GenerateJWT(*user)
	if err != nil {
		writer.WriteHeader(http.StatusInternalServerError)
		return
	}

	writer.Header().Set("Content-Type", "application/json")
	writer.WriteHeader(http.StatusOK)
	json.NewEncoder(writer).Encode(map[string]string{
		"token": token,
	})
}

// Logout
func (handler *UserHandler) Logout(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]string{
		"message": "Logged out successfully",
	})
}

// Dobavljanje korisnika po ID
func (handler *UserHandler) Get(writer http.ResponseWriter, req *http.Request) {
	idStr := mux.Vars(req)["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		writer.Header().Set("Content-Type", "application/json")
		writer.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(writer).Encode(map[string]string{
			"error": "Invalid UUID format",
		})
		return
	}

	user, err := handler.UserService.FindUser(id)
	writer.Header().Set("Content-Type", "application/json")
	if err != nil {
		writer.WriteHeader(http.StatusNotFound)
		json.NewEncoder(writer).Encode(map[string]string{
			"error": "User not found",
		})
		return
	}

	writer.WriteHeader(http.StatusOK)
	json.NewEncoder(writer).Encode(user)
}
