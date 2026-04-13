package dto

type UpdateProfileDTO struct {
	Name         string `json:"name"`
	Surname      string `json:"surname"`
	ProfileImage string `json:"profileimage"`
	Description  string `json:"description"`
	Motto        string `json:"motto"`
}
