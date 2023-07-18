package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PersonneDto {

    private Long id;
    private String nomComplet;
    private String username;
    private LocalDate dateCreation;
    private String adresseMail;
    private String motDePasse;
    private String statut;
    private ProfileDto profile;
    private String image;

}



