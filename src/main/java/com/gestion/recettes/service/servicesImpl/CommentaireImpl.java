package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.CommentaireDto;
import com.gestion.recettes.dto.CommentairePersonneContactDto;
import com.gestion.recettes.dto.PersonneDto;
import com.gestion.recettes.entities.Commentaire;
import com.gestion.recettes.entities.Personne;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.CommentaireRepo;
import com.gestion.recettes.repos.PersonneRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gestion.recettes.service.servicesImpl.PersonneImpl.convertToPersonne;
import static com.gestion.recettes.service.servicesImpl.PersonneImpl.convertToPersonneDTO;

@Service
public class CommentaireImpl implements CommentaireService {
    private final CommentaireRepo commentaireRepo;
    
    private final RecetteRepo recetteRepo;
    
    private final PersonneRepo personneRepo;
    private static RecetteRepo recetteRepoStat;
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentaireImpl(CommentaireRepo commentaireRepo, RecetteRepo recetteRepo, PersonneRepo personneRepo, JdbcTemplate jdbcTemplate) {
        this.commentaireRepo = commentaireRepo;
        this.recetteRepo = recetteRepo;
        this.personneRepo = personneRepo;
        this.jdbcTemplate = jdbcTemplate;
        recetteRepoStat = recetteRepo;
    }

    @Override
    public CommentaireDto creer(CommentaireDto commentaireDTO, Long recetteId, Long personneId) {
        Commentaire commentaire = convertToCommentaire(commentaireDTO);

        if (personneId != null) {
            Optional<Personne> existingPersonne = personneRepo.findById(personneId);
            existingPersonne.ifPresent(commentaire::setProprietaire);
        }

        if (recetteId != null) {
            Optional<Recette> existingRecette = recetteRepo.findById(recetteId);
            if (existingRecette.isPresent()) {
                Recette recette = existingRecette.get();
                commentaire.setRecette(recette);
                commentaire = commentaireRepo.save(commentaire);
                recette.getCommentaires().add(commentaire); // Add the Commentaire to the Recette's list of Commentaires

                // Save the updated Recette along with the new Commentaire
                recetteRepo.save(recette);

                // Convert the saved Recette and its Commentaires to DTOs
                return convertToCommentaireDTO(commentaire);
            }
        }

        return null; // or handle the case where recetteId is null or Recette not found
    }




    @Override
    public List<CommentaireDto> lireTous() {
        List<Commentaire> commentaires = commentaireRepo.findAll();
        return convertToCommentaireDtoList(commentaires);
    }

    @Override
    public CommentaireDto lire(Long id) {
        Optional<Commentaire> optionalCommentaire = commentaireRepo.findById(id);
        if(optionalCommentaire.isPresent()){
            Commentaire commentaire = optionalCommentaire.get();
            return convertToCommentaireDTO(commentaire);
        }else{
            System.out.println("Ce commentaire est introuvable");
            return null;
        }
    }
    @Override
    public CommentaireDto modifier(Long id, CommentaireDto commentaireDTO) {
        if(commentaireRepo.existsById(id))
        {
            Commentaire commentaire = commentaireRepo.getById(id);

            commentaire.setMessage(commentaireDTO.getMessage());
            commentaire.setNote(commentaireDTO.getNote());
            if (commentaireDTO.getCreatedAt() != null)
                commentaire.setCreatedAt(commentaireDTO.getCreatedAt());
            Commentaire updatedCommentaire = commentaireRepo.save(commentaire);
            return convertToCommentaireDTO(updatedCommentaire);
        }else{
            System.out.println("Ce commentaire est introuvable");
        }
        return commentaireDTO;
    }

    @Override
    public Boolean supprimer(Long id) {
        if(commentaireRepo.existsById(id))
        {
            commentaireRepo.deleteById(id);
        }else {
            System.out.println("Ce commentaire est introuvable");
        }
        return true;
    }

    @Override
    public List<CommentaireDto> commentairesRecette(Long idRecette) {
        return convertToCommentaireDtoList(commentaireRepo.findByRecetteId(idRecette));
    }

    @Override
    public CommentaireDto contactCommentaire(CommentairePersonneContactDto commentairePersonneContactDto) {
        Commentaire commentaire =  new Commentaire();
        Personne personne = new Personne();
        String messageObject;
        if (commentairePersonneContactDto.getObjet() != null){
            //<::> play role of seperator btw the subject and message or comment
            messageObject = commentairePersonneContactDto.getObjet() + "<::>" + commentairePersonneContactDto.getMessage();
        }
        else
            messageObject = commentairePersonneContactDto.getMessage();

        commentaire.setMessage(messageObject);
        personne.setUsername(commentairePersonneContactDto.getUsername());
        personne.setAddressMailContact(commentairePersonneContactDto.getEmail());
        personne.setStatut("visitor");
        personneRepo.save(personne);
        PersonneDto personneDto = convertToPersonneDTO(personne);
        commentaire.setProprietaire(convertToPersonne(personneDto));

        commentaireRepo.save(commentaire);
        return CommentaireImpl.convertToCommentaireDTO(commentaire);
    }

    @Override
    public CommentairePersonneContactDto contactes() {
        String sql = "SELECT commentaire.id FROM commentaire INNER JOIN personne ON personne.id = commentaire.proprietaire_id WHERE personne.statut = 'visitor'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long idCommentaire = rs.getLong("id");
            Optional<Commentaire> commentaireOptional = commentaireRepo.findById(idCommentaire);
            if (commentaireOptional.isPresent()){
                CommentaireDto commentaireDto = convertToCommentaireDTO(commentaireOptional.get());
                CommentairePersonneContactDto commentairePersonneContactDto = new CommentairePersonneContactDto();
                commentairePersonneContactDto.setId(commentaireDto.getId());
                commentairePersonneContactDto.setUsername(commentaireDto.getProprietaire().getUsername());
                commentairePersonneContactDto.setEmail(commentaireDto.getProprietaire().getAddressMailContact());
                if (commentaireDto.getCreatedAt() != null)
                    commentairePersonneContactDto.setCreatedAt(commentaireDto.getCreatedAt());
                String message = commentaireDto.getMessage();
                if (message.contains("<::>")) {
                    String[] parties = message.split("<::>", 2);
                    String objet = parties[0];
                    message = (parties.length > 1) ? parties[1] : "";
                    commentairePersonneContactDto.setObjet(objet);
                }
                commentairePersonneContactDto.setMessage(message);


                return commentairePersonneContactDto;
            }
            return null; // Handle the case where no result is found
        }).stream().findFirst().orElse(null); // Get the first result or null if empty
    }


    public static CommentaireDto convertToCommentaireDTO(Commentaire commentaire) {
        CommentaireDto commentaireDTO = new CommentaireDto();
        commentaireDTO.setId(commentaire.getId());
        commentaireDTO.setMessage(commentaire.getMessage());
        commentaireDTO.setNote(commentaire.getNote());
        commentaireDTO.setCreatedAt(commentaire.getCreatedAt());
        if(commentaire.getCreateurRecette() != null) {
            commentaireDTO.setCreateurRecette(convertToPersonneDTO(commentaire.getCreateurRecette()));
        } if (commentaire.getApprobateurRecette() != null) {
            commentaireDTO.setApprobateurRecette(convertToPersonneDTO(commentaire.getApprobateurRecette()));
        } if(commentaire.getProprietaire() != null){
            commentaireDTO.setProprietaire(convertToPersonneDTO(commentaire.getProprietaire()));
        }if(commentaire.getRecette() != null){
            commentaireDTO.setRecetteId(commentaire.getRecette().getId());
        }
        return commentaireDTO;
    }

    public static Commentaire convertToCommentaire(CommentaireDto commentaireDTO) {
        Commentaire commentaire = new Commentaire();
        commentaire.setId(commentaireDTO.getId());
        commentaire.setMessage(commentaireDTO.getMessage());
        commentaire.setNote(commentaireDTO.getNote());
        commentaire.setCreatedAt(commentaireDTO.getCreatedAt());
        if(commentaireDTO.getCreateurRecette() != null) {
            commentaire.setCreateurRecette(convertToPersonne(commentaireDTO.getCreateurRecette()));
        } if (commentaireDTO.getApprobateurRecette() != null) {
            commentaire.setApprobateurRecette(convertToPersonne(commentaireDTO.getApprobateurRecette()));
        } if(commentaireDTO.getRecetteId() != null){
            if (recetteRepoStat.existsById(commentaireDTO.getRecetteId())){
                Recette recette = recetteRepoStat.getReferenceById(commentaireDTO.getRecetteId());
                commentaire.setRecette(recette);
            }
        } if(commentaireDTO.getProprietaire() != null){
            commentaire.setProprietaire(convertToPersonne(commentaireDTO.getProprietaire()));
        }
        return commentaire;
    }
    public static List<CommentaireDto> convertToCommentaireDtoList(List<Commentaire> commentaireList){

        List<CommentaireDto> commentaireDtoList = new ArrayList<>();
        for(Commentaire commentaire : commentaireList){
            commentaireDtoList.add(convertToCommentaireDTO(commentaire));
        }
        return commentaireDtoList;
    }

    public static List<Commentaire> convertToCommentaireList(List<CommentaireDto> commentaireDtos) {
        List<Commentaire> commentaires = new ArrayList<>();
        for (CommentaireDto commentaireDto : commentaireDtos) {
            Commentaire commentaire = convertToCommentaire(commentaireDto);
            commentaires.add(commentaire);
        }
        return commentaires;
    }
}
