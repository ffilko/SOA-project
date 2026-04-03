package handler

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"stakeholders.xws.com/service"
)

type ProfileHandler struct {
	ProfileService *service.ProfileService
}

func RegisterProfileRouter(router *mux.Router, handler *ProfileHandler) {
	router.HandleFunc("/profile/{id}", handler.GetById).Methods("GET")
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
