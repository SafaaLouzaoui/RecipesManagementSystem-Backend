package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentaireDto {

    private Long id;
    private String message;
    private BigDecimal note;
    private Timestamp createdAt;
    private PersonneDto createurRecette;
    private PersonneDto proprietaire;
    private Long recetteId;
    private PersonneDto approbateurRecette;


}
