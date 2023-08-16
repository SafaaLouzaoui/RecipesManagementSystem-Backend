package com.gestion.recettes.config;


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
  private String username;
  private String adresseMail;
  private String motDePasse;
  private String statut;
  private Role role;
  private String image;
}