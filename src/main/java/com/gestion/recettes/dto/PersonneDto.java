package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.gestion.recettes.entities.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PersonneDto {

    private Long id;
    private String nomComplet;
    private String username;
    private LocalDate dateCreation;
    private String adresseMail;
    private String addressMailContact;
    private String motDePasse;
    private String statut;
    private Role role;
    private String image;

}



