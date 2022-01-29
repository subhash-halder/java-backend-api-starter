package com.letsflyby.java_api.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.letsflyby.java_api.model.Role;
import com.letsflyby.java_api.model.User;
import com.letsflyby.java_api.repo.RoleRepo;
import com.letsflyby.java_api.repo.UserRepo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserServiceImpl
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepo userRepo;
  private final RoleRepo roleRepo;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User saveUser(User user) {
    log.info("Saving new user to the database");
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepo.save(user);
  }

  @Override
  public Role saveRole(Role role) {
    log.info("Saving new role to the database");
    return roleRepo.save(role);
  }

  @Override
  public void addRoleToUser(String email, String roleName) {
    log.info("Adding new role {} to the user {}", email, roleName);
    User user = userRepo.findByEmail(email);
    Role role = roleRepo.findByName(roleName);
    user.getRoles().add(role);
  }

  @Override
  public User getUser(String email) {
    log.info("Get user by email {}", email);
    return userRepo.findByEmail(email);
  }

  @Override
  public User getUser(UUID id) {
    log.info("Get user by userId {}", id);
    return userRepo.getById(id);
  }

  @Override
  public List<User> getUsers() {
    log.info("Get all users");
    return userRepo.findAll();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(username);
    if (user == null) {
      log.error("User not found for email: {}", username);
      throw new UsernameNotFoundException("User not found");
    } else {
      log.info("User found for email: {}", username);
    }
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles().forEach(role -> {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    });
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
  }

}
