package com.letsflyby.java_api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * USERS
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;
  @Column(unique = true)
  private String email;
  private String name;
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  private Collection<Role> roles = new ArrayList<>();
}
