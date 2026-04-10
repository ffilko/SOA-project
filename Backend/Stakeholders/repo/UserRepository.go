package repo

import (
	"github.com/google/uuid"
	"gorm.io/gorm"
	"stakeholders.xws.com/model"
)

type UserRepository struct {
	DatabaseConnection *gorm.DB
}

func (repo *UserRepository) FindById(id uuid.UUID) (model.User, error) {
	user := model.User{}
	dbResult := repo.DatabaseConnection.First(&user, "id = ?", id)
	if dbResult.Error != nil {
		return user, dbResult.Error
	}
	return user, nil
}

func (repo *UserRepository) FindByUsername(username string) (model.User, error) {
	user := model.User{}
	dbResult := repo.DatabaseConnection.First(&user, "username = ?", username)
	if dbResult.Error != nil {
		return user, dbResult.Error
	}
	return user, nil
}

func (repo *UserRepository) CreateUser(user *model.User) error {
	dbResult := repo.DatabaseConnection.Create(user)
	if dbResult.Error != nil {
		return dbResult.Error
	}
	println("Rows affected : ", dbResult.RowsAffected)
	return nil
}

func (repo *UserRepository) GetAllUsers() ([]model.User, error) {
	var users []model.User
	dbResult := repo.DatabaseConnection.Find(&users)
	if dbResult.Error != nil {
		return nil, dbResult.Error
	}
	return users, nil
}
