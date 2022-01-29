package com.letsflyby.java_api.service;

import java.util.List;
import java.util.UUID;

import com.letsflyby.java_api.model.Role;
import com.letsflyby.java_api.model.User;

/**
 * UserService
 */
public interface UserService {

  User saveUser(User user);

  Role saveRole(Role role);

  void addRoleToUser(String email, String roleName);

  User getUser(String email);

  User getUser(UUID id);

  List<User> getUsers();
}
