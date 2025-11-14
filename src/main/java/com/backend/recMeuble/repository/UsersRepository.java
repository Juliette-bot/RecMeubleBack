package com.backend.recMeuble.repository;

import com.backend.recMeuble.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);   // <-- Optional veut dire qu'il êut ne pas en trouver
    boolean existsByMail(String mail);
}
