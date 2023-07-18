package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {

    private Long id;
    private String nom;
    private String description;
    private String media;
    private List<Long> recettesIds = new ArrayList<>();
    private Long ingredientIdReference;
    private List<Long> quantitesIds = new ArrayList<>();
}
