package com.example.online.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.online.Repository.UserRepository;
import com.example.online.model.User;
@Service
public class UserService {
     @Autowired private UserRepository userRepo;

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
    public Optional<User> updateUser(Long id, User updatedUser) {
    return userRepo.findById(id).map(existingUser -> {
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPasswordHash(updatedUser.getPasswordHash());
        existingUser.setRole(updatedUser.getRole());
        return userRepo.save(existingUser);
    });
}
}
