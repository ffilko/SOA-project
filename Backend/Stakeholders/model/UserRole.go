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

func UserRoleFromString(s string) UserRole {
	switch s {
	case "Tourist":
		return Tourist
	case "Guide":
		return Guide
	case "Administrator":
		return Administrator
	default:
		return Tourist
	}
}
