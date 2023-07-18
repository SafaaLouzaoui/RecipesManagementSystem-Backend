package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorieDto {

    private Long idCat;
    private String nomCat;
    private String descriptionCat;
    private String media;

    //private List<RecetteDto> recettes = new ArrayList<>();
    private List<Long> recetteIds = new ArrayList<>();
}