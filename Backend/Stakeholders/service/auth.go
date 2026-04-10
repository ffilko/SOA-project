package service

import (
	"os"

	"github.com/golang-jwt/jwt/v5"
	"stakeholders.xws.com/model"
)

var secretKey []byte

func GetSecretKey() []byte {
	if len(secretKey) == 0 {
		secret := os.Getenv("JWT_SECRET")
		if secret == "" {
			secret = "dev-secret"
		}
		secretKey = []byte(secret)
	}
	return secretKey
}

func GenerateJWT(user model.User) (string, error) {
	claims := jwt.MapClaims{
		"user_id": user.ID.String(),
		"role":    model.UserRoleToString(user.Role),
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString(GetSecretKey())
}
