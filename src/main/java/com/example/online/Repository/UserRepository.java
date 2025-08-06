package com.example.online.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> findByEmail(String email);
}

