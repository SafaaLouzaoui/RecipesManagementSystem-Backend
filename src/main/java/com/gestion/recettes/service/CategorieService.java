package com.gestion.recettes.service;

import com.gestion.recettes.dto.CategorieDto;

import java.util.List;

public interface CategorieService {
    CategorieDto creer(CategorieDto categorieDTO);
    List<CategorieDto> lireTous();
    CategorieDto lire(Long id);
    CategorieDto modifier(Long id, CategorieDto categorieDTO);
    Boolean supprimer(Long id);
}
