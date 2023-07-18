package com.gestion.recettes.service;

import com.gestion.recettes.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    ProfileDto creer(ProfileDto profileDto);
    ProfileDto lire(Long id);
    List<ProfileDto> lireTous();
    ProfileDto modifier(Long id,ProfileDto profileDto);
    Boolean supprimer(Long id);



}
