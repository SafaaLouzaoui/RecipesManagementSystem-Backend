package com.gestion.recettes.service;

import com.gestion.recettes.dto.EtapeDto;

import java.util.List;

public interface EtapeService {
    EtapeDto creer(EtapeDto etapeDTO);
    List<EtapeDto> lireTous();
    EtapeDto lire(Long id);
    EtapeDto modifier(Long id, EtapeDto etapeDTO);
    Boolean supprimer(Long id);
}
