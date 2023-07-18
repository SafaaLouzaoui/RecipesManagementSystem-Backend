package com.gestion.recettes.service;

import com.gestion.recettes.dto.TypeRelationDto;

import java.util.List;

public interface TypeRelationService {

    TypeRelationDto creer(TypeRelationDto typeRelationDto);
    TypeRelationDto lire(Long id);
    List<TypeRelationDto> lireTous();
    TypeRelationDto modifier(Long id,TypeRelationDto typeRelationDto);
    Boolean supprimer(Long id);
}
