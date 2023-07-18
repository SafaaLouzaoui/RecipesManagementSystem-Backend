package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRelationDto {

    private Long id;
    private String description;
    private List<RecetteDto> recettesLiees = new ArrayList<>();
    private List<RecetteDto> recettesLieesA = new ArrayList<>();

}
