package com.letsflyby.java_api.repo;

import java.util.UUID;

import com.letsflyby.java_api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepo
 */
public interface UserRepo extends JpaRepository<User, UUID> {
  User findByEmail(String email);

}
