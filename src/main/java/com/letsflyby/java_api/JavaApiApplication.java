package com.letsflyby.java_api;

import java.util.ArrayList;

import com.letsflyby.java_api.model.Role;
import com.letsflyby.java_api.model.User;
import com.letsflyby.java_api.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JavaApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(JavaApiApplication.class, args);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CommandLineRunner run(UserService memberService) {
    return args -> {
      memberService.saveRole(new Role(null, "USER"));
      memberService.saveRole(new Role(null, "ADMIN"));
      memberService.saveRole(new Role(null, "SUPER_ADMIN"));

      memberService.saveUser(new User(null, "subhash@letsflyby.com", "Subhash Halder", "1234", new ArrayList<>()));
      memberService.saveUser(new User(null, "akash@letsflyby.com", "Akash Goswami", "1234", new ArrayList<>()));
      memberService.saveUser(new User(null, "navendu@letsflyby.com", "Navendu Duari", "1234", new ArrayList<>()));

      memberService.addRoleToUser("subhash@letsflyby.com", "USER");
      memberService.addRoleToUser("subhash@letsflyby.com", "ADMIN");
      memberService.addRoleToUser("subhash@letsflyby.com", "SUPER_ADMIN");
      memberService.addRoleToUser("akash@letsflyby.com", "USER");
      memberService.addRoleToUser("navendu@letsflyby.com", "USER");
    };
  }

}
