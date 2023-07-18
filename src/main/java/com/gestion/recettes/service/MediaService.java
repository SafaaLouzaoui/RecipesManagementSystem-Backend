package com.gestion.recettes.service;

import com.gestion.recettes.dto.MediaDto;

import java.util.List;

public interface MediaService {
    MediaDto creer(MediaDto mediaDTO);
    MediaDto lire(Long id);
    List<MediaDto> lireTous();
    MediaDto modifier(Long id, MediaDto mediaDTO);
    Boolean supprimer(Long id);
}
