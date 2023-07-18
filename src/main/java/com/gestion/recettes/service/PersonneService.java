package com.gestion.recettes.service;

import com.gestion.recettes.dto.*;
import com.gestion.recettes.response.LoginResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;


public interface PersonneService {

    PersonneDto inscrire(PersonneDto personneDTO);

    PersonneDto lire(Long id);
    LoginResponse loginPersonne(LoginDto loginDTO, HttpSession session);

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
    boolean promouvoirUtilisateurVersModerateur(Long utilisateurId);
    List<PersonneDto> mesFollowers(Long id);
}
