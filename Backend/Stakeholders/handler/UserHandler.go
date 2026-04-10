package handler

import (
	"encoding/json"
	"net/http"
	"strings"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"stakeholders.xws.com/dto"
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
	router.HandleFunc("/users", handler.GetAll).Methods("GET")
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

func (handler *UserHandler) GetAll(writer http.ResponseWriter, req *http.Request) {
	authHeader := req.Header.Get("Authorization")
	tokenStr := strings.TrimPrefix(authHeader, "Bearer ")

	token, err := jwt.Parse(tokenStr, func(t *jwt.Token) (interface{}, error) {
		return service.GetSecretKey(), nil
	})

	if err != nil || !token.Valid {
		writer.WriteHeader(http.StatusUnauthorized)
		return
	}

	claims, _ := token.Claims.(jwt.MapClaims)
	if claims["role"] != "Administrator" {
		writer.WriteHeader(http.StatusForbidden)
		return
	}

	allUsers, err := handler.UserService.GetAllUsers()
	if err != nil {
		writer.WriteHeader(http.StatusInternalServerError)
		return
	}

	var users []dto.UserResponse
	for i := 0; i < len(allUsers); i++ {
		currentUser := allUsers[i]
		user := dto.UserResponse{
			ID:       currentUser.ID,
			Username: currentUser.Username,
			Email:    currentUser.Email,
			Role:     currentUser.Role,
		}

		users = append(users, user)
	}

	writer.Header().Set("Content-Type", "application/json")
	writer.WriteHeader(http.StatusOK)
	json.NewEncoder(writer).Encode(users)
}
