package com.gestion.recettes.controllers;

import com.gestion.recettes.config.AuthenticationRequest;
import com.gestion.recettes.config.AuthenticationResponse;
import com.gestion.recettes.config.RegisterRequest;
import com.gestion.recettes.dto.LoginDto;
import com.gestion.recettes.dto.PersonneDto;
import com.gestion.recettes.response.LoginResponse;
import com.gestion.recettes.service.PersonneService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/utilisateurs")

public class PersonneController {

    @Autowired
    private PersonneService personneService;

    @PostMapping(path = "/inscrire")
    public ResponseEntity<AuthenticationResponse> register(
    	      @RequestBody RegisterRequest request
    	  ) {
    	    return ResponseEntity.ok(personneService.inscrire(request));
    	  }
    
//    @PostMapping(path = "/inscrire")
//    public ResponseEntity<PersonneDto> inscrirePersonne(@RequestBody PersonneDto personneDTO) {
//        PersonneDto personneInscrite = personneService.inscrire(personneDTO);
//        return new ResponseEntity<>(personneInscrite, HttpStatus.CREATED);
//    }
    
    @PostMapping(path = "/connexion")
    public ResponseEntity<AuthenticationResponse> loginPersonne(
  	      @RequestBody AuthenticationRequest request
  	  ) {
  	    return ResponseEntity.ok(personneService.loginPersonne(request));
  	  }

//    @PostMapping(path = "/connexion")
//    public ResponseEntity<LoginResponse> loginPersonne(@RequestBody LoginDto loginDto, HttpSession session) {
//        LoginResponse loginResponse = personneService.loginPersonne(loginDto, session);
//        if (loginResponse.getId() != null) {
//            return ResponseEntity.ok().body(loginResponse);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
//        }
//    }

    @GetMapping(path="/lire/{id}")
    public ResponseEntity<PersonneDto> lire(@PathVariable("id") Long id){
        PersonneDto personneDTO = personneService.lire(id);
        if(personneDTO != null){
            return ResponseEntity.ok(personneDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(path="/lireTous")
    public List<PersonneDto> getAllUsers()
    {
        List<PersonneDto> allUsers = personneService.lireTous();
        return allUsers;
    }

    @PutMapping(path = "/modifier/{id}")
    public PersonneDto modifier(@PathVariable("id")Long id, @RequestBody PersonneDto personneDTO)
    {
        return personneService.modifier(id, personneDTO);

    }
    @PostMapping("/bloquer/{id}")
    public ResponseEntity<String> bloquerCompte(@PathVariable("id") Long id) {
        boolean compteBloque = personneService.bloquerCompte(id);
        if (compteBloque) {
            return ResponseEntity.ok("Compte bloqué avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Impossible de bloquer le compte.");
        }
    }

    @PostMapping("/activer/{id}")
    public ResponseEntity<String> activerCompte(@PathVariable("id") Long id) {
        boolean compteActive = personneService.activerCompte(id);
        if (compteActive) {
            return ResponseEntity.ok("Compte activé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ce compte est déja activé.");
        }
    }
    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean deleteUser(@PathVariable (value="id") Long id)
    {
        Boolean deletedUser = personneService.supprimer(id);
        return deletedUser;
    }

    @PostMapping("/abonner/{personneId}/{abonneId}")
    public ResponseEntity<String> abonner(@PathVariable("personneId") Long personneId, @PathVariable("abonneId") Long abonneId) {
        personneService.abonner(personneId, abonneId);
        return ResponseEntity.ok("Abonnement effectué avec succès.");
    }

    @PostMapping(path = "/signalerRecette/{idPersonne}/{idRecette}")
    public ResponseEntity<String> signalerRecette(@PathVariable("idPersonne") Long idPersonne, @PathVariable("idRecette") Long idRecette) {
        try {
            personneService.signalerRecette(idPersonne, idRecette);
            return ResponseEntity.ok("Recette signalée avec succès.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/approuver")
    public ResponseEntity<String> approuverRecette(@PathVariable("id") Long idRecette, @RequestParam("personneId") Long idPersonne) {
        personneService.approuverRecette(idPersonne, idRecette);
        return ResponseEntity.ok("La recette a été approuvée avec succès.");
    }

    @PutMapping("/{id}/deconnexion")
    public ResponseEntity<String> logoutPersonne(@PathVariable("id") Long id, HttpSession session) {
        personneService.logoutPersonne(id, session);
        return ResponseEntity.ok("Déconnexion réussie.");
    }
//    @PostMapping("/promote/{utilisateurId}")
//    public ResponseEntity<String> promouvoirUtilisateurVersModerateur(@PathVariable Long utilisateurId) {
//        boolean success = personneService.promouvoirUtilisateurVersModerateur(utilisateurId);
//
//        if (success) {
//            return ResponseEntity.ok("L'utilisateur a été promu en modérateur avec succès.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
//        }
//    }

    @GetMapping(path = "mesFollowers/{utilisateurId}")
    public List<PersonneDto> mesFollowers(@PathVariable Long id){
        return personneService.mesFollowers(id);
    }
}