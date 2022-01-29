package com.letsflyby.java_api.repo;

import java.util.UUID;

import com.letsflyby.java_api.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RoleRepo
 */
public interface RoleRepo extends JpaRepository<Role, UUID> {
  Role findByName(String name);

}
