package handler

import (
	"encoding/json"
	"net/http"
	"strings"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"stakeholders.xws.com/dto"
	"stakeholders.xws.com/service"
)

type ProfileHandler struct {
	ProfileService *service.ProfileService
}

func RegisterProfileRouter(router *mux.Router, handler *ProfileHandler) {
	router.HandleFunc("/profile/{id}", handler.GetById).Methods("GET")
	router.HandleFunc("/profile/{id}", handler.Update).Methods("PUT")
}

func (handler *ProfileHandler) GetById(writer http.ResponseWriter, req *http.Request) {
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

	profile, err := handler.ProfileService.FindUser(id)
	writer.Header().Set("Content-Type", "application/json")
	if err != nil {
		writer.WriteHeader(http.StatusNotFound)
		json.NewEncoder(writer).Encode(map[string]string{
			"error": "Profile not found",
		})
		return
	}

	writer.WriteHeader(http.StatusOK)
	json.NewEncoder(writer).Encode(profile)
}

func (handler *ProfileHandler) Update(writer http.ResponseWriter, req *http.Request) {
	// Provjeri token
	authHeader := req.Header.Get("Authorization")
	tokenStr := strings.TrimPrefix(authHeader, "Bearer ")

	token, err := jwt.Parse(tokenStr, func(t *jwt.Token) (interface{}, error) {
		return service.GetSecretKey(), nil
	})

	if err != nil || !token.Valid {
		writer.WriteHeader(http.StatusUnauthorized)
		return
	}

	// Izvuci user_id iz tokena
	claims, _ := token.Claims.(jwt.MapClaims)
	tokenUserID, ok := claims["user_id"].(string)
	if !ok {
		writer.WriteHeader(http.StatusUnauthorized)
		return
	}

	// Provjeri da li mijenja SVOJ profil
	idStr := mux.Vars(req)["id"]
	if tokenUserID != idStr {
		writer.WriteHeader(http.StatusForbidden)
		return
	}

	id, err := uuid.Parse(idStr)
	if err != nil {
		writer.Header().Set("Content-Type", "application/json")
		writer.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(writer).Encode(map[string]string{
			"error": "Invalid UUID format",
		})
		return
	}

	var updatedDTO dto.UpdateProfileDTO
	err = json.NewDecoder(req.Body).Decode(&updatedDTO)
	if err != nil {
		writer.WriteHeader(http.StatusBadRequest)
		return
	}

	profile, err := handler.ProfileService.Update(id, updatedDTO)
	if err != nil {
		writer.Header().Set("Content-Type", "application/json")
		writer.WriteHeader(http.StatusNotFound)
		json.NewEncoder(writer).Encode(map[string]string{
			"error": err.Error(),
		})
		return
	}

	writer.Header().Set("Content-Type", "application/json")
	writer.WriteHeader(http.StatusOK)
	json.NewEncoder(writer).Encode(profile)
}
