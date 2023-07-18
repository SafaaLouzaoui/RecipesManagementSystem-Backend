package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotCleDto {

    private Long id;
    private String mot;
    private List<Long> recetteIds = new ArrayList<>();
}
