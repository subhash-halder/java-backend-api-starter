package com.letsflyby.java_api.api;

import java.net.URI;
import java.util.List;

import com.letsflyby.java_api.model.User;
import com.letsflyby.java_api.model.Role;
import com.letsflyby.java_api.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * UserController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
  private final UserService userService;

  @GetMapping("/users")
  public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok().body(userService.getUsers());
  }

  @PostMapping("/user")
  public ResponseEntity<User> saveUser(@RequestBody User user) {
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString());
    return ResponseEntity.created(uri).body(userService.saveUser(user));
  }

  @PostMapping("/role")
  public ResponseEntity<Role> saveRole(@RequestBody Role role) {
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role").toUriString());
    return ResponseEntity.created(uri).body(userService.saveRole(role));
  }

  @PutMapping("/user_role")
  public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
    userService.addRoleToUser(form.getEmail(), form.getRoleName());
    return ResponseEntity.ok().build();
  }
}

@Data
class RoleToUserForm {
  private String email;
  private String roleName;
}
