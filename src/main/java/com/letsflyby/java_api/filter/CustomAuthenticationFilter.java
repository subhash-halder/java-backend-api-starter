package com.letsflyby.java_api.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsflyby.java_api.model.User;
import com.letsflyby.java_api.service.UserService;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CustomAuthenticationFilter
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    log.info("Loggin attempt with username: {}", username);
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        username, password);
    return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authentication) throws IOException, ServletException {
    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    User dbUser = userService.getUser(user.getUsername());
    // properly use the secret
    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
    String accessToken = JWT.create()
        .withSubject(dbUser.getId().toString())
        .withExpiresAt(new Date(System.currentTimeMillis() + 600000))
        .withIssuer(request.getRequestURL().toString())
        .withClaim("roles",
            user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .sign(algorithm);
    String refreshToken = JWT.create()
        .withSubject(dbUser.getId().toString())
        .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
        .withIssuer(request.getRequestURL().toString())
        .sign(algorithm);
    // response.setHeader("access_token", accessToken);
    // response.setHeader("refresh_token", refreshToken);
    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
