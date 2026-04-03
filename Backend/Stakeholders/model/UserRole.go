package model

type UserRole int

const (
	Tourist UserRole = iota
	Guide
	Administrator
)

func UserRoleToString(ur UserRole) string {
	switch ur {
	case Tourist:
		return "Tourist"
	case Guide:
		return "Guide"
	case Administrator:
		return "Administrator"
	default:
		return "Unknown"
	}
}
