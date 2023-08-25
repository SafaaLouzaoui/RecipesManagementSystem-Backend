package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecetteDto {
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
    private PersonneDto utilisateurCreateur;
    private PersonneDto utilisateurApprobateur;
    private List<PersonneDto> signalants = new ArrayList<>();
    private List<MediaDto> medias = new ArrayList<>();
    private List<MediaDto> mediasBesoin = new ArrayList<>();
    private List<BesoinDto> besoins = new ArrayList<>();
    private List<MotCleDto> motCles = new ArrayList<>();
    private List<IngredientDto> ingredients = new ArrayList<>();
    private List<EtapeDto> etapes = new ArrayList<>();
    private List<CategorieDto> categories = new ArrayList<>();
    private TypeRelationDto typeRelation;
    private TypeRelationDto typeRel;
    private List<QuantiteDto> quantites = new ArrayList<>();
    private List<CommentaireDto> commentaires = new ArrayList<>();
}