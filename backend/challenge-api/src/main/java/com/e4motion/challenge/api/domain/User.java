package com.e4motion.challenge.api.domain;

import java.util.Set;

import lombok.Data;

//@Entity
//@Table(name = "user")
@Data
public class User {

   //@Id
   //@Column(name = "id")
   //@GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   //@Column(name = "user_id", length = 50, unique = true)
   private String userId;
   
   //@Column(name = "username", length = 50)
   private String username;

   //@Column(name = "password", length = 100)
   private String password;

   //@Column(name = "email", length = 100)
   private String email;
   
   //@Column(name = "activated")
   private boolean activated;

   //@ManyToMany
   //@JoinTable(
   //   name = "user_authority",
   //   joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
   //   inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
   private Set<Authority> authorities;
   
   public User(String userId, String password) {
       this.userId = userId;
       this.password = password;
   }
   
   public User(String userId, String password, String username, String email, Set<Authority> authorities) {
       this.userId = userId;
       this.password = password;
       this.username = username;
       this.email = email;
       this.authorities = authorities;
   }
   
}
