package com.gestion.recettes.service;

import com.gestion.recettes.dto.IngredientDto;

import java.util.List;

public interface IngredientService {
    IngredientDto creer(IngredientDto ingredientDTO);
    List<IngredientDto> lireTous();
    IngredientDto lire(Long id);
    IngredientDto modifier(Long id, IngredientDto ingredientDTO);
    Boolean supprimer(Long id);
}
