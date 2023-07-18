package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.MediaDto;
import com.gestion.recettes.entities.Media;
import com.gestion.recettes.entities.Personne;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.MediaRepo;
import com.gestion.recettes.repos.PersonneRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MediaImpl implements MediaService {
    @Autowired
    private MediaRepo mediaRepo;
    private final RecetteRepo recetteRepo;
    private static RecetteRepo recetteRepoStat;
    private final PersonneRepo personneRepo;
    private static PersonneRepo personneRepoStat;

    public MediaImpl(RecetteRepo recetteRepo, PersonneRepo personneRepo) {
        this.recetteRepo = recetteRepo;
        recetteRepoStat = recetteRepo;
        this.personneRepo = personneRepo;
    }


    @Override
    public MediaDto creer(MediaDto mediaDTO) {
        Media media = convertToMedia(mediaDTO);
        mediaRepo.save(media);
        return convertToMediaDTO(media);
    }

    @Override
    public MediaDto lire(Long id){

        Optional<Media> optionalMedia = mediaRepo.findById(id);
        if(optionalMedia.isPresent()){
            Media media = optionalMedia.get();
            return convertToMediaDTO(media);
        }else{
            System.out.println("Ce media n'existe pas");
            return null;
        }
    }
    @Override
    public List<MediaDto> lireTous() {
        List<Media> getMedias = mediaRepo.findAll();
        return convertToMediaDTOList(getMedias);
    }

    @Override
    public MediaDto modifier(Long id, MediaDto mediaDTO) {
        Optional<Media> optionalMedia = mediaRepo.findById(id);
        if (optionalMedia.isPresent() && mediaDTO != null) {
            Media media = optionalMedia.get();
            if (mediaDTO.getUrl() != null) {
                media.setUrl(mediaDTO.getUrl());
            }
            if (mediaDTO.getPersonneId() != null) {
                if (personneRepo.existsById(mediaDTO.getPersonneId())){
                    Personne personne = personneRepo.getReferenceById(mediaDTO.getPersonneId());
                    media.setPersonne(personne);
                }
            }
            if (mediaDTO.getMediaType() != null) {
                media.setMediaType(MediaTypeImpl.convertToMediaType(mediaDTO.getMediaType()));
            }
            if (mediaDTO.getRecetteId() != null) {
                Optional<Recette> optionalRecette = recetteRepo.findById(mediaDTO.getRecetteId());
                if (optionalRecette.isPresent()) {
                    Recette recette = optionalRecette.get();
                    media.setRecette(recette);
                }
            }
            Media updatedMedia = mediaRepo.save(media);
            return convertToMediaDTO(updatedMedia);
        } else {
            System.out.println("Ce media n'existe pas");
        }
        return mediaDTO;
    }


    @Override
    public Boolean supprimer(Long id) {
        if(mediaRepo.existsById(id)){
            mediaRepo.deleteById(id);
        }else{
            System.out.println("Ce media n'existe pas");
        }
        return true;
    }

    public static MediaDto convertToMediaDTO(Media media) {
        MediaDto mediaDTO = new MediaDto();
        mediaDTO.setId(media.getId());
        mediaDTO.setUrl(media.getUrl());
        if(media.getPersonne() != null) {
            mediaDTO.setPersonneId(media.getPersonne().getId());
        }
        if(media.getMediaType() != null) {
            mediaDTO.setMediaType(MediaTypeImpl.convertToMediaTypeDto(media.getMediaType()));
        }
        if(media.getRecette() != null) {
            mediaDTO.setRecetteId(media.getRecette().getId());
        }
        return mediaDTO;
    }

    public static Media convertToMedia(MediaDto mediaDTO) {
        Media media = new Media();
        media.setId(mediaDTO.getId());
        media.setUrl(mediaDTO.getUrl());
        if(mediaDTO.getPersonneId() != null) {
            if (personneRepoStat.existsById(mediaDTO.getPersonneId())){
                Personne personne = personneRepoStat.getReferenceById(mediaDTO.getPersonneId());
                media.setPersonne(personne);
            }
        }
        if(mediaDTO.getMediaType() != null) {
            media.setMediaType(MediaTypeImpl.convertToMediaType(mediaDTO.getMediaType()));
        }
        if(mediaDTO.getRecetteId() != null) {
            if (recetteRepoStat.existsById(mediaDTO.getRecetteId())){
                Recette recette = recetteRepoStat.getReferenceById(mediaDTO.getRecetteId());
                media.setRecette(recette);

            }
        }
        return media;
    }

    public static List<MediaDto> convertToMediaDTOList(List<Media> mediaList) {
        List<MediaDto> mediaDtoList = new ArrayList<>();
        for (Media media : mediaList) {
            mediaDtoList.add(convertToMediaDTO(media));
        }
        return mediaDtoList;
    }

    public static List<Media> convertToMediaList(List<MediaDto> mediaDtoList) {
        List<Media> mediaList = new ArrayList<>();
        for (MediaDto mediaDTO : mediaDtoList) {
            mediaList.add(convertToMedia(mediaDTO));
        }
        return mediaList;
    }
}
