package saga

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

type DeleteUserSagaState int

const (
	StateStart DeleteUserSagaState = iota
	StateUserDeleted
	StateBlogsDeleted
	StateFollowersDeleted
	StateCompleted
	StateFailed
)

type DeleteUserSaga struct {
	UserID       string
	State        DeleteUserSagaState
	blogsUrl     string
	followersUrl string
}

type SavedUser struct {
	ID       string
	Username string
	Email    string
	Password string
	Role     string
}

func NewDeleteUserSaga(userId, blogsUrl, followersUrl string) *DeleteUserSaga {
	return &DeleteUserSaga{
		UserID:       userId,
		State:        StateStart,
		blogsUrl:     blogsUrl,
		followersUrl: followersUrl,
	}
}

func (s *DeleteUserSaga) Execute(
	deleteUser func(id string) (*SavedUser, error),
	restoreUser func(u *SavedUser) error,
) error {
	log.Printf("[SAGA] Pokrećem DeleteUser SAGA za korisnika %s", s.UserID)

	// Obriši korisnika u Stakeholders
	savedUser, err := deleteUser(s.UserID)
	if err != nil {
		s.State = StateFailed
		log.Printf("[SAGA] KORAK 1 NEUSPEŠAN: %v", err)
		return fmt.Errorf("saga failed at step 1 (delete user): %w", err)
	}
	s.State = StateUserDeleted
	log.Printf("[SAGA] Korak 1 OK: korisnik obrisan iz Stakeholders")

	// Obriši blogove korisnika u Blogs servisu
	if err := s.deleteBlogsByUser(); err != nil {
		log.Printf("[SAGA] KORAK 2 NEUSPEŠAN: %v — pokrećem kompenzaciju", err)
		s.compensate_restoreUser(savedUser, restoreUser)
		s.State = StateFailed
		return fmt.Errorf("saga failed at step 2 (delete blogs): %w", err)
	}
	s.State = StateBlogsDeleted
	log.Printf("[SAGA] Korak 2 OK: blogovi obrisani iz Blogs servisa")

	// Obriši follow veze u Followers servisu
	if err := s.deleteFollowersByUser(); err != nil {
		log.Printf("[SAGA] KORAK 3 NEUSPEŠAN: %v — pokrećem kompenzaciju", err)
		s.compensate_restoreBlogs()
		s.compensate_restoreUser(savedUser, restoreUser)
		s.State = StateFailed
		return fmt.Errorf("saga failed at step 3 (delete followers): %w", err)
	}
	s.State = StateFollowersDeleted

	s.State = StateCompleted
	log.Printf("[SAGA] DeleteUser SAGA USPEŠNO ZAVRŠENA za korisnika %s", s.UserID)
	return nil
}

func (s *DeleteUserSaga) deleteBlogsByUser() error {
	url := fmt.Sprintf("%s/api/blogs/user/%s", s.blogsUrl, s.UserID)
	req, err := http.NewRequest(http.MethodDelete, url, nil)
	if err != nil {
		return err
	}

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return fmt.Errorf("blogs service nedostupan: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK && resp.StatusCode != http.StatusNoContent {
		return fmt.Errorf("blogs servis vratio status %d", resp.StatusCode)
	}
	return nil
}

func (s *DeleteUserSaga) deleteFollowersByUser() error {
	url := fmt.Sprintf("%s/api/follow/user/%s", s.followersUrl, s.UserID)
	req, err := http.NewRequest(http.MethodDelete, url, nil)
	if err != nil {
		return err
	}

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return fmt.Errorf("followers service nedostupan: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK && resp.StatusCode != http.StatusNoContent {
		return fmt.Errorf("followers servis vratio status %d", resp.StatusCode)
	}
	return nil
}

func (s *DeleteUserSaga) compensate_restoreUser(saved *SavedUser, restoreUser func(u *SavedUser) error) {
	log.Printf("[SAGA] Vraćam korisnika %s u Stakeholders", s.UserID)
	if err := restoreUser(saved); err != nil {
		log.Printf("[SAGA] GREŠKA pri vraćanju korisnika: %v", err)
	} else {
		log.Printf("[SAGA] Korisnik uspešno vraćen")
	}
}

func (s *DeleteUserSaga) compensate_restoreBlogs() {
	url := fmt.Sprintf("%s/api/blogs/user/%s/restore", s.blogsUrl, s.UserID)
	body, _ := json.Marshal(map[string]string{"userId": s.UserID})

	resp, err := http.Post(url, "application/json", bytes.NewBuffer(body))
	if err != nil {
		log.Printf("[SAGA] GREŠKA pri vraćanju blogova: %v", err)
		return
	}
	defer resp.Body.Close()
	log.Printf("[SAGA] Blogs restore odgovor: %d", resp.StatusCode)
}
