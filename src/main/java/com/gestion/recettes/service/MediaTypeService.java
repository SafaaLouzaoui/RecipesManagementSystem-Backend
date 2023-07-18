package com.gestion.recettes.service;

import com.gestion.recettes.dto.MediaTypeDto;

import java.util.List;

public interface MediaTypeService {

    MediaTypeDto creer(MediaTypeDto mediaTypeDto);
    MediaTypeDto lire(Long id);
    List<MediaTypeDto> lireTous();

    MediaTypeDto modifer(Long id,MediaTypeDto mediaTypeDto);


    Boolean supprimer(Long id);


}
