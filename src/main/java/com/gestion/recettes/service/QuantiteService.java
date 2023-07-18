package com.gestion.recettes.service;

import com.gestion.recettes.dto.*;

import java.util.List;

public interface QuantiteService {
    QuantiteDto creer(QuantiteDto quantiteDto);
    QuantiteDto lire(Long id);

    List<QuantiteDto> lireTous();

    QuantiteDto modifier(Long id,QuantiteDto quantiteDto);

    Boolean supprimer(Long id);

}
