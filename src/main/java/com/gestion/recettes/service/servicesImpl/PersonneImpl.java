package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.*;
import com.gestion.recettes.entities.Personne;
import com.gestion.recettes.entities.Profile;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.PersonneRepo;
import com.gestion.recettes.repos.ProfileRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.response.LoginResponse;
import com.gestion.recettes.service.PersonneService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
public class PersonneImpl implements PersonneService {
    private final PersonneRepo personneRepo;
    private final ProfileRepo profileRepo;
    private final RecetteRepo recetteRepo;
    private final PasswordEncoder passwordEncoder;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonneImpl(PersonneRepo personneRepo, ProfileRepo profileRepo, RecetteRepo recetteRepo, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.personneRepo = personneRepo;
        this.profileRepo = profileRepo;
        this.recetteRepo = recetteRepo;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public PersonneDto inscrire(PersonneDto personneDTO) {

        int codeProfilUtilisateur = 1;
        Optional<Profile> existingProfile = profileRepo.findByCode(codeProfilUtilisateur);

        Profile profile = existingProfile.orElseGet(() -> {

            Profile newProfile = new Profile();
            newProfile.setCode(codeProfilUtilisateur);
            newProfile.setDescription("utilisateur");
            return profileRepo.save(newProfile);
        });


        Personne personne = convertToPersonne(personneDTO);
        personne.setProfile(profile);
        personne.setStatut("active");

        if (personne.getMotDePasse() != null){
            String encodedPassword = passwordEncoder.encode(personne.getMotDePasse());
            personne.setMotDePasse(encodedPassword);
        }


        personneRepo.save(personne);

        return convertToPersonneDTO(personne);
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
    public LoginResponse loginPersonne(LoginDto loginDto, HttpSession session) {
        Personne personne1 = personneRepo.findByAdresseMailAndStatutNot(loginDto.getAdresseMail(), "visitor");
        if (personne1 != null) {
            String password = loginDto.getMotDePasse();
            String encodedPassword = personne1.getMotDePasse();
            String statut = personne1.getStatut();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<Personne> employee = personneRepo.findByAdresseMailAndMotDePasse(loginDto.getAdresseMail(), encodedPassword);
                if (employee.isPresent()) {
                    if ((!statut.equals("blocked")) || (!statut.equals("deleted"))) {
                        Personne personne = employee.get();
                        String role = personne.getProfile().getDescription();
                        Long id = personne.getId();

                        if (role.equals("moderateur")) {
                            session.setAttribute("userId", id);
                            return new LoginResponse(id,"Login Success (Moderator)", true);
                        } else if (role.equals("utilisateur")) {
                            session.setAttribute("userId", id);
                            return new LoginResponse(id,"Login Success (User)", true);
                        } else if (role.equals("administrateur")) {
                            session.setAttribute("userId", id);
                            return new LoginResponse(id,"Login Success (Administrator)", true);
                        } else {
                            return new LoginResponse(null,"Unknown Role", false);
                        }
                    } else {
                        return new LoginResponse(null,"This account has been blocked or deleted", false);
                    }
                } else {
                    return new LoginResponse(null,"Login Failed", false);
                }
            } else {
                return new LoginResponse(null,"password Not Match", false);
            }
        } else {
            return new LoginResponse(null,"Email not exists", false);
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
            personne.setUsername(personneDTO.getUsername());
            personne.setDateCreation(LocalDate.now());
            personne.setAdresseMail(personneDTO.getAdresseMail());
            String encodedPassword = passwordEncoder.encode(personneDTO.getMotDePasse());
            personne.setMotDePasse(encodedPassword);
            personne.setStatut("active");

            if (personneDTO.getProfile() != null) {
                personne.setProfile(ProfileImpl.convertToProfile(personneDTO.getProfile()));
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

            if (personne.getProfile().getCode() == 3) {
                recette.setStatut(1);
                recette.setDatePublication(LocalDate.now());
                recette.setUtilisateurApprobateur(personne);
                recetteRepo.save(recette);
            } else {
                System.out.println("Seuls les modérateurs peuvent approuver une recette.");
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

            if (personne.getProfile().getCode() == 3) {
                recette.setStatut(0);
                recette.setUtilisateurApprobateur(personne);
                recetteRepo.save(recette);
            } else {
                System.out.println("Seuls les modérateurs peuvent refuser une recette.");
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
    public boolean promouvoirUtilisateurVersModerateur(Long utilisateurId) {
        Optional<Personne> optionalUtilisateur = personneRepo.findById(utilisateurId);

        if (optionalUtilisateur.isPresent()) {
            Personne utilisateur = optionalUtilisateur.get();
            Optional<Profile> optionalModerateurProfile = profileRepo.findByCode(2);

            if (optionalModerateurProfile.isPresent()) {
                Profile moderateurProfile = optionalModerateurProfile.get();
                utilisateur.setProfile(moderateurProfile);
                personneRepo.save(utilisateur);
                return true;
            } else {
                System.out.println("Le profil de modérateur n'existe pas.");
                return false;
            }
        } else {
            System.out.println("L'utilisateur n'existe pas.");
            return false;
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
        personneDTO.setUsername(personne.getUsername());
        personneDTO.setDateCreation(personne.getDateCreation());
        personneDTO.setAdresseMail(personne.getAdresseMail());
        personneDTO.setMotDePasse(personne.getMotDePasse());
        personneDTO.setStatut(personne.getStatut());
        if (personne.getProfile() != null) {
            personneDTO.setProfile(ProfileImpl.convertToProfileDto(personne.getProfile()));
        }

        return personneDTO;
    }
    public static Personne convertToPersonne(PersonneDto personneDTO) {
        Personne personne = new Personne();
        personne.setId(personneDTO.getId());
        personne.setNomComplet(personneDTO.getNomComplet());
        personne.setUsername(personneDTO.getUsername());
        personne.setDateCreation(LocalDate.now());
        personne.setAdresseMail(personneDTO.getAdresseMail());
        personne.setMotDePasse(personneDTO.getMotDePasse());
        personne.setStatut(personneDTO.getStatut());

        if (personneDTO.getProfile() != null) {
            personne.setProfile(ProfileImpl.convertToProfile(personneDTO.getProfile()));
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
