package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecetteRefDto {
    private Long id;
    private String nom;
    private String description;
    private String origine;
    private int dureePreparation;
    private int dureeCuisson;
    private int nbrPersonnes;
    private String visibilitee;
    private int statut;
    private LocalDate dateSoumission;
    private LocalDate datePublication;
}
