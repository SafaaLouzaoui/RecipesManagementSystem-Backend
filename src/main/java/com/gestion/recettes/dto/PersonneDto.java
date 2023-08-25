package com.gestion.recettes.dto;

import com.gestion.recettes.entities.Commentaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.gestion.recettes.entities.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PersonneDto {

    private Long id;
    private String nomComplet;
    private String user_name;
    private LocalDate dateCreation;
    private String adresseMail;
    private String addressMailContact;
    private String motDePasse;
    private String statut;
    private Role role;
    private String image;
    private List<PersonneDto> abonnees; //TODO (state : not working yet) Create a new type of personne named Followers

}



