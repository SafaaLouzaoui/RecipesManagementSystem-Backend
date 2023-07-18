package com.gestion.recettes.service;

import com.gestion.recettes.dto.*;

import java.util.List;

public interface MotCleService {
    MotCleDto creer(MotCleDto motCleDto);

    MotCleDto lire(Long id);

    List<MotCleDto> lireTous();

    MotCleDto modifier(Long id, MotCleDto motCleDto);


    Boolean supprimer(Long id);

}
