package com.gestion.recettes.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuantiteDto {

    private Long id;
    private String uniteDeMesure;
    private Double nombre;
    private Long ingredientId;
    private Long recetteId;

}
