package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtapeDto {

    private Long id;
    private int ordre;
    private String description;
    private int duree;
    private String media;
    private Long recetteId;
}
