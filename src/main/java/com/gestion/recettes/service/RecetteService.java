package com.gestion.recettes.service;

import com.gestion.recettes.dto.CommentaireDto;
import com.gestion.recettes.dto.RecetteDto;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface RecetteService {

    RecetteDto creer(RecetteDto recetteDto, HttpSession session);
    RecetteDto lire(Long id);
    List <RecetteDto> lireTous();
    RecetteDto modifier(Long id,RecetteDto recetteDto);
    Boolean supprimer(Long id);
    List <RecetteDto> recettesByCategorie(Long id);
    List<RecetteDto> searchRecettesByNom(String nom);
    List<RecetteDto> mesRecettes(Long id);
    List<RecetteDto> recettesSignalees();
}
