package handler

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"stakeholders.xws.com/model"
)

func (handler *UserHandler) RegisterWithSaga(writer http.ResponseWriter, req *http.Request) {
	var user model.User
	if err := json.NewDecoder(req.Body).Decode(&user); err != nil {
		writer.WriteHeader(http.StatusBadRequest)
		return
	}

	log.Printf("[SAGA-CHOREOGRAPHY] Korak 1: Registracija korisnika %s", user.Username)

	if err := handler.UserService.Create(&user); err != nil {
		log.Printf("[SAGA-CHOREOGRAPHY] Korak 1 NEUSPEŠAN: %v", err)
		writer.WriteHeader(http.StatusExpectationFailed)
		return
	}

	log.Printf("[SAGA-CHOREOGRAPHY] Korak 1 OK — korisnik kreiran, ID: %s", user.ID)

	if err := publishUserRegisteredEvent(user.ID.String()); err != nil {
		log.Printf("[SAGA-CHOREOGRAPHY] Korak 2 NEUSPEŠAN (Followers nedostupan): %v", err)
		log.Printf("[SAGA-CHOREOGRAPHY] KOMPENZACIJA: brišem korisnika %s iz Stakeholders", user.ID)

		if rollbackErr := handler.UserService.DeleteUser(user.ID); rollbackErr != nil {
			log.Printf("[SAGA-CHOREOGRAPHY] KOMPENZACIJA NEUSPEŠNA: %v — sistem u nekonzistentnom stanju!", rollbackErr)
		} else {
			log.Printf("[SAGA-CHOREOGRAPHY] KOMPENZACIJA OK: korisnik obrisan")
		}

		writer.WriteHeader(http.StatusServiceUnavailable)
		json.NewEncoder(writer).Encode(map[string]string{
			"error": "Registracija neuspešna — Followers servis nedostupan",
		})
		return
	}

	log.Printf("[SAGA-CHOREOGRAPHY] Korak 2 OK — event poslat Followers servisu")

	writer.Header().Set("Content-Type", "application/json")
	writer.WriteHeader(http.StatusCreated)
	json.NewEncoder(writer).Encode(map[string]string{
		"message": "Korisnik registrovan — SAGA u toku (asinhrono)",
		"userId":  user.ID.String(),
	})
}

func publishUserRegisteredEvent(userID string) error {
	followersURL := "http://followers-service:8082/api/follow/user"

	payload := map[string]string{"userId": userID}
	body, _ := json.Marshal(payload)

	resp, err := http.Post(followersURL, "application/json", bytes.NewBuffer(body))
	if err != nil {
		return fmt.Errorf("followers servis nedostupan: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusCreated && resp.StatusCode != http.StatusOK {
		return fmt.Errorf("followers servis vratio grešku: status %d", resp.StatusCode)
	}

	return nil
}
