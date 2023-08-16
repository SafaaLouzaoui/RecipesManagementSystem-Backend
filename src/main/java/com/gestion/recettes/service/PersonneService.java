package com.gestion.recettes.service;

import com.gestion.recettes.config.AuthenticationRequest;
import com.gestion.recettes.config.AuthenticationResponse;
import com.gestion.recettes.config.RegisterRequest;
import com.gestion.recettes.dto.*;
import com.gestion.recettes.response.LoginResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;


public interface PersonneService {

    AuthenticationResponse inscrire(RegisterRequest request);

    PersonneDto lire(Long id);
    AuthenticationResponse loginPersonne(AuthenticationRequest request);

    List<PersonneDto> lireTous();

    PersonneDto modifier(Long id, PersonneDto personneDTO);
    Boolean bloquerCompte(Long id);
    Boolean activerCompte(Long id);
    Boolean supprimerCompte(Long id);
    Boolean supprimer(Long id);
    void abonner(Long personneId, Long abonneId);
    void signalerRecette(Long idPersonne, Long idRecette);
    void approuverRecette(Long idPersonne, Long idRecette);
    void refuserRecette(Long idPersonne, Long idRecette);
    void logoutPersonne(Long id, HttpSession session);
    List<PersonneDto> mesFollowers(Long id);
}
