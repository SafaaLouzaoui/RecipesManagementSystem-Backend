package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDto {
    private Long Id;
    private String url;
    //private PersonneDTO personne;
    private Long personneId;
    private MediaTypeDto mediaType;
    private Long recetteId;
}