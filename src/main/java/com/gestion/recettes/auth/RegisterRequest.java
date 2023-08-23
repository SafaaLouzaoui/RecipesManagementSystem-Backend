package com.gestion.recettes.auth;


import com.gestion.recettes.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String nomComplet;
  private String user_name;
  private String adresseMail;
  private String motDePasse;
  private Role role;
}