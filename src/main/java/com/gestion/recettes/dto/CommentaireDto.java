package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentaireDto {

    private Long id;
    private String message;
    private BigDecimal note;
    private PersonneDto createurRecette;
    private PersonneDto proprietaire;
    private Long recetteId;
    private PersonneDto approbateurRecette;

}
