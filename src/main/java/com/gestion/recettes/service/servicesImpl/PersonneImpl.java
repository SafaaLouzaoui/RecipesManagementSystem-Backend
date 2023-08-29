package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.config.JwtService;
import com.gestion.recettes.dto.*;
import com.gestion.recettes.entities.Personne;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.entities.Role;
import com.gestion.recettes.repos.PersonneRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.PersonneService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
public class PersonneImpl implements PersonneService {
    private final PersonneRepo personneRepo;
    private static PersonneRepo personneRepoStat;
    private final RecetteRepo recetteRepo;
    private final PasswordEncoder passwordEncoder;
    private JdbcTemplate jdbcTemplate;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


    @Autowired
    public PersonneImpl(PersonneRepo personneRepo, RecetteRepo recetteRepo, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.personneRepo = personneRepo;
        this.recetteRepo = recetteRepo;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
        personneRepoStat = personneRepo;
        //this.jwtService=jwtService;
        //this.authenticationManager=authenticationManager;
    }


    @Override
    public PersonneDto lire(Long id) {
        Optional<Personne> optionalPersonne = personneRepo.findById(id);

        if(optionalPersonne.isPresent()){
            Personne personne = optionalPersonne.get();
            return convertToPersonneDTO(personne);
        }else{
            System.out.println("Ce compte n'existe pas");
            return null;
        }
    }


    @Override
    public List<PersonneDto> lireTous() {
        List<Personne> getPersonnes = personneRepo.findAll();
        return convertToPersonneDTOList(getPersonnes);
    }

    @Override
    public PersonneDto modifier(Long id, PersonneDto personneDTO) {
        Optional<Personne> optionalPersonne = personneRepo.findById(id);
        if (optionalPersonne.isPresent()) {
            Personne personne = optionalPersonne.get();
            personne.setNomComplet(personneDTO.getNomComplet());
            personne.setUser_name(personneDTO.getUser_name());
            personne.setDateCreation(LocalDate.now());
            personne.setAdresseMail(personneDTO.getAdresseMail());
            if (personneDTO.getMotDePasse() != null){
                String encodedPassword = passwordEncoder.encode(personneDTO.getMotDePasse());
                personne.setMotDePasse(encodedPassword);
            }
            personne.setStatut("active");

            if (personneDTO.getRole() != null) {
                personne.setRole(personneDTO.getRole());
            }

            Personne updatedPersonne = personneRepo.save(personne);
            return convertToPersonneDTO(updatedPersonne);
        } else {
            System.out.println("Ce compte n'existe pas");
        }
        return personneDTO;
    }

    @Override
    public Boolean bloquerCompte(Long id) {
        PersonneDto personne = lire(id);
        if (personne == null) {
            System.out.println("Le compte n'existe pas");
            return null;
        }
        if (personne.getStatut().equals("blocked")) {
            System.out.println("Ce compte est déjà bloqué");
            return false;
        }
        personne.setStatut("blocked");
        modifier(id, personne);
        return true;
    }
    @Override
    public Boolean activerCompte(Long id) {
        PersonneDto personne = lire(id);
        if (personne == null) {
            System.out.println("Le compte n'existe pas");
            return null;
        }
        if (personne.getStatut().equals("activated")) {
            System.out.println("Ce compte est déjà activé");
            return false;
        }
        personne.setDateCreation(LocalDate.now());
        personne.setStatut("activated");
        modifier(id, personne);
        return true;
    }
    @Override
    public Boolean supprimerCompte(Long id) {
        Optional<Personne> optionalPersonne = personneRepo.findById(id);

        if (optionalPersonne.isPresent()) {
            Personne personne = optionalPersonne.get();

            personne.setStatut("deleted");

            personneRepo.save(personne);
            return true;
        } else {
            System.out.println("Ce compte n'existe pas !!");
            return false;
        }
    }

    @Override
    public Boolean supprimer(Long id) {
        if (personneRepo.existsById(id)) {
            personneRepo.deleteById(id);
        } else {
            System.out.println("Ce compte n'existe pas !!");
        }
        return true;
    }
    @Override
    public void abonner(Long personneId, Long abonneId) {
        Optional<Personne> optionalPersonne = personneRepo.findById(personneId);
        Optional<Personne> optionalAbonne = personneRepo.findById(abonneId);

        if (optionalPersonne.isPresent() && optionalAbonne.isPresent()) {
            Personne personne = optionalPersonne.get();
            Personne abonne = optionalAbonne.get();

            personne.getAbonnees().add(abonne);
            personneRepo.save(personne);
        } else {
            throw new EntityNotFoundException("Personne or Abonne not found");
        }
    }
    @Override
    public void signalerRecette(Long idPersonne, Long idRecette) {
        Optional<Personne> optionalPersonne = personneRepo.findById(idPersonne);
        Optional<Recette> optionalRecette = recetteRepo.findById(idRecette);

        if (optionalPersonne.isPresent() && optionalRecette.isPresent()) {
            Personne personne = optionalPersonne.get();
            Recette recette = optionalRecette.get();

            personne.getRecettesSignalees().add(recette);
            personneRepo.save(personne);
        } else {
            throw new EntityNotFoundException("La personne ou la recette n'existe pas.");
        }
    }

    @Override
    public void approuverRecette(Long idPersonne, Long idRecette) {
        Optional<Personne> optionalPersonne = personneRepo.findById(idPersonne);
        Optional<Recette> optionalRecette = recetteRepo.findById(idRecette);

        if (optionalPersonne.isPresent() && optionalRecette.isPresent()) {
            Personne personne = optionalPersonne.get();
            Recette recette = optionalRecette.get();

            if (personne.getRole() == Role.ADMIN) {
                recette.setStatut(1);
                recette.setDatePublication(LocalDate.now());
                recette.setUtilisateurApprobateur(personne);
                recetteRepo.save(recette);
            } else {
                System.out.println("Seul l'ADMIN peut approuver une recette.");
            }
        } else {
            throw new EntityNotFoundException("La personne ou la recette n'existe pas.");
        }
    }

    @Override
    public void refuserRecette(Long idPersonne, Long idRecette) {
        Optional<Personne> optionalPersonne = personneRepo.findById(idPersonne);
        Optional<Recette> optionalRecette = recetteRepo.findById(idRecette);

        if (optionalPersonne.isPresent() && optionalRecette.isPresent()) {
            Personne personne = optionalPersonne.get();
            Recette recette = optionalRecette.get();

            if (personne.getRole() == Role.ADMIN) {
                recette.setStatut(0);
                recette.setUtilisateurApprobateur(personne);
                recetteRepo.save(recette);
            } else {
                System.out.println("Seuls l'admin peut refuser une recette.");
            }
        } else {
            throw new EntityNotFoundException("La personne ou la recette n'existe pas.");
        }
    }
    @Override
    public void logoutPersonne(Long id, HttpSession session) {
        Optional<Personne> optionalPersonne = personneRepo.findById(id);

        if (optionalPersonne.isPresent()) {
            Personne personne = optionalPersonne.get();

            personneRepo.save(personne);

            session.invalidate();
        } else {
            throw new EntityNotFoundException("La personne n'existe pas.");
        }
    }

    @Override
    public List<PersonneDto> mesFollowers(Long id) {
        String sql = "SELECT abonnees_id FROM personne_abonnee WHERE personne_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long abonnee_id = rs.getLong("abonnees_id");

            Optional<Personne> optionalPersonne = personneRepo.findById(abonnee_id);
            PersonneDto personneDTO = optionalPersonne.map(PersonneImpl::convertToPersonneDTO).orElse(null);

            return personneDTO;
        });
    }

    public static PersonneDto convertToPersonneDTO(Personne personne) {
        PersonneDto personneDTO = new PersonneDto();

        personneDTO.setId(personne.getId());
        personneDTO.setNomComplet(personne.getNomComplet());
        personneDTO.setUser_name(personne.getUsername());
        personneDTO.setDateCreation(personne.getDateCreation());
        personneDTO.setAdresseMail(personne.getAdresseMail());
        personneDTO.setMotDePasse(personne.getMotDePasse());
        personneDTO.setStatut(personne.getStatut());
        if (personne.getRole() != null) {
            personneDTO.setRole(personne.getRole());
        }

        return personneDTO;
    }
    public static Personne convertToPersonne(PersonneDto personneDTO) {
        Personne personne = new Personne();
        personne.setId(personneDTO.getId());
        personne.setNomComplet(personneDTO.getNomComplet());
        personne.setUser_name(personneDTO.getUser_name());
        personne.setDateCreation(LocalDate.now());
        personne.setAdresseMail(personneDTO.getAdresseMail());
        personne.setMotDePasse(personneDTO.getMotDePasse());
        personne.setStatut(personneDTO.getStatut());
        if (personneDTO.getRole() != null) {
            personne.setRole(personneDTO.getRole());
        }

        return personne;
    }

    public static List<PersonneDto> convertToPersonneDTOList(List<Personne> personnes) {
        List<PersonneDto> personneDtoList = new ArrayList<>();
        for (Personne personne : personnes) {
            PersonneDto personneDTO = convertToPersonneDTO(personne);
            personneDtoList.add(personneDTO);
        }
        return personneDtoList;
    }

    public static List<Personne> convertToPersonneList(List<PersonneDto> personneDtoList) {
        List<Personne> personnes = new ArrayList<>();
        for (PersonneDto personneDTO : personneDtoList) {
            personnes.add(convertToPersonne(personneDTO));
        }
        return personnes;
    }
}
