package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BesoinDto {

    private Long id;
    private String nom;
    private  String type;
    private  String description;
    private String media;
    private List<RecetteDto> recettes = new ArrayList<>();
}
