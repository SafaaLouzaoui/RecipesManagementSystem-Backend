package com.gestion.recettes.service;

import com.gestion.recettes.dto.BesoinDto;

import java.util.List;

public interface BesoinService {

    BesoinDto creer(BesoinDto besoinDTO);
    List<BesoinDto> lireTous();
    BesoinDto lire(Long id);
    BesoinDto modifier(Long id, BesoinDto besoinDTO);
    Boolean supprimer(Long id);
}
