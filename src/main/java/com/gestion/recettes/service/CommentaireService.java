package com.gestion.recettes.service;

import com.gestion.recettes.dto.CommentaireDto;
import com.gestion.recettes.dto.CommentairePersonneContactDto;

import java.util.List;

public interface CommentaireService {
    CommentaireDto creer(CommentaireDto commentaireDTO, Long recetteId, Long personneId);
    List<CommentaireDto> lireTous();
    CommentaireDto lire(Long id);

    CommentaireDto modifier(Long id, CommentaireDto commentaireDTO);
    Boolean supprimer(Long id);
    List<CommentaireDto> commentairesRecette(Long idRecette);
    CommentaireDto contactCommentaire(CommentairePersonneContactDto commentairePersonneContactDto);
    CommentairePersonneContactDto contactes();
}
