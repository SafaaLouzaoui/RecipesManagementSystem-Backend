package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.MediaTypeDto;
import com.gestion.recettes.entities.MediaType;
import com.gestion.recettes.repos.MediaTypeRepo;
import com.gestion.recettes.service.MediaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MediaTypeImpl implements MediaTypeService {

    @Autowired
    private MediaTypeRepo mediaTypeRepo;


    @Override
    public MediaTypeDto creer(MediaTypeDto mediaTypeDto) {
        MediaType mediaType = convertToMediaType(mediaTypeDto);
        mediaTypeRepo.save(mediaType);
        return convertToMediaTypeDto(mediaType);
    }

    @Override
    public MediaTypeDto lire(Long id) {
        Optional<MediaType> optionalMediaType = mediaTypeRepo.findById(id);
        if (optionalMediaType.isPresent()) {
            MediaType mediaType = optionalMediaType.get();
            return convertToMediaTypeDto(mediaType);
        } else {
            System.out.println("Ce type de média n'existe pas");
            return null;
        }
    }
    @Override
    public List<MediaTypeDto> lireTous() {
        List<MediaType> getTypes = mediaTypeRepo.findAll();
        return convertToMediaTypeDtoList(getTypes);
    }

    @Override
    public MediaTypeDto modifer(Long id, MediaTypeDto mediaTypeDto) {
        if (mediaTypeRepo.existsById(id)) {
            MediaType mediaType = mediaTypeRepo.getById(id);
            mediaType.setNom(mediaTypeDto.getNom());
            MediaType updatedMediaType = mediaTypeRepo.save(mediaType);
            return convertToMediaTypeDto(updatedMediaType);
        } else {
            System.out.println("Ce type de média n'existe pas");
        }
        return mediaTypeDto;
    }


    @Override
    public Boolean supprimer(Long id) {
        if(mediaTypeRepo.existsById(id)){
            mediaTypeRepo.deleteById(id);
        }else{
            System.out.println("Ce type de media n'existe pas");
        }
        return true;
    }

    public static MediaTypeDto convertToMediaTypeDto(MediaType mediaType) {
        MediaTypeDto mediaTypeDto = new MediaTypeDto();
        mediaTypeDto.setId(mediaType.getId());
        mediaTypeDto.setNom(mediaType.getNom());
        return mediaTypeDto;
    }
    public static MediaType convertToMediaType(MediaTypeDto mediaTypeDto) {
        MediaType mediaType = new MediaType();
        mediaType.setId(mediaTypeDto.getId());
        mediaType.setNom(mediaTypeDto.getNom());
        return mediaType;
        }

    public static List<MediaTypeDto> convertToMediaTypeDtoList(List<MediaType> types){
        List<MediaTypeDto> mediaTypeDtos = new ArrayList<>();
        for(MediaType mediaType : types){
            mediaTypeDtos.add(convertToMediaTypeDto(mediaType));
        }
        return mediaTypeDtos;
    }
}
