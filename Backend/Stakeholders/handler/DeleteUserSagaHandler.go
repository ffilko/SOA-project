package handler

import (
	"encoding/json"
	"net/http"
	"strings"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"stakeholders.xws.com/saga"
	"stakeholders.xws.com/service"
)

func (handler *UserHandler) DeleteUserWithSaga(writer http.ResponseWriter, req *http.Request) {
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

	idStr := mux.Vars(req)["id"]
	if _, err := uuid.Parse(idStr); err != nil {
		writer.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(writer).Encode(map[string]string{"error": "Invalid UUID"})
		return
	}

	sagaInstance := saga.NewDeleteUserSaga(
		idStr,
		"http://blogs-service:8080",
		"http://followers-service:8082",
	)

	err = sagaInstance.Execute(
		func(id string) (*saga.SavedUser, error) {
			return handler.UserService.SoftDeleteUser(id)
		},
		func(u *saga.SavedUser) error {
			return handler.UserService.RestoreUser(u)
		},
	)

	if err != nil {
		writer.Header().Set("Content-Type", "application/json")
		writer.WriteHeader(http.StatusInternalServerError)
		json.NewEncoder(writer).Encode(map[string]string{
			"error":   "SAGA failed — sistem vraćen u konzistentno stanje",
			"details": err.Error(),
		})
		return
	}

	writer.Header().Set("Content-Type", "application/json")
	writer.WriteHeader(http.StatusOK)
	json.NewEncoder(writer).Encode(map[string]string{
		"message": "Korisnik uspešno obrisan iz svih servisa",
	})
}
