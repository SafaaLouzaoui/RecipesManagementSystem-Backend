package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.TypeRelationDto;
import com.gestion.recettes.entities.TypeRelation;
import com.gestion.recettes.repos.TypeRelationRepo;
import com.gestion.recettes.service.TypeRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TypeRelationImpl implements TypeRelationService {

    private final TypeRelationRepo typeRelationRepo;

    @Autowired
    public TypeRelationImpl(TypeRelationRepo typeRelationRepo) {
        this.typeRelationRepo = typeRelationRepo;
    }

    @Override
    public TypeRelationDto creer(TypeRelationDto typeRelationDto) {
        TypeRelation typeRelation = convertToTypeRelation(typeRelationDto);
        typeRelationRepo.save(typeRelation);
        return convertToTypeRelationDto(typeRelation);
    }

    @Override
    public TypeRelationDto lire(Long id) {
        Optional<TypeRelation> optionalTypeRelation = typeRelationRepo.findById(id);
        if (optionalTypeRelation.isPresent()) {
            TypeRelation typeRelation = optionalTypeRelation.get();
            return convertToTypeRelationDto(typeRelation);
        } else {
            System.out.println("Ce type de relation n'existe pas");
            return null;
        }
    }

    @Override
    public List<TypeRelationDto> lireTous() {

        List<TypeRelation> getTypes = typeRelationRepo.findAll();
        return convertToTypeRelationDtoList(getTypes);
    }

    @Override
    public TypeRelationDto modifier(Long id, TypeRelationDto typeRelationDto) {
        if (typeRelationRepo.existsById(id)) {
            TypeRelation typeRelation = typeRelationRepo.getById(id);
            typeRelation.setDescription(typeRelationDto.getDescription());
            TypeRelation updatedType = typeRelationRepo.save(typeRelation);
            return convertToTypeRelationDto(updatedType);
        } else {
            System.out.println("Ce type de relation n'existe pas");
            return null;
        }
    }

    @Override
    public Boolean supprimer(Long id) {
        if (typeRelationRepo.existsById(id)) {
            typeRelationRepo.deleteById(id);
            return true;
        } else {
            System.out.println("Ce type de relation n'existe pas");
            return false;
        }
    }

    public static TypeRelationDto convertToTypeRelationDto(TypeRelation typeRelation) {
        TypeRelationDto typeRelationDto = new TypeRelationDto();
        typeRelationDto.setId(typeRelation.getId());
        typeRelationDto.setDescription(typeRelation.getDescription());
        if(typeRelation.getRecettesLiees() != null) {
            typeRelationDto.setRecettesLiees(RecetteImpl.convertToRecetteDtoList(typeRelation.getRecettesLiees()));
        }
        if(typeRelation.getRecettesLieesA() != null) {
            typeRelationDto.setRecettesLieesA(RecetteImpl.convertToRecetteDtoList(typeRelation.getRecettesLieesA()));
        }
        return typeRelationDto;
    }

    public static TypeRelation convertToTypeRelation(TypeRelationDto typeRelationDto) {
        TypeRelation typeRelation = new TypeRelation();
        typeRelation.setId(typeRelationDto.getId());
        typeRelation.setDescription(typeRelationDto.getDescription());
        if(typeRelationDto.getRecettesLiees() != null) {
            typeRelation.setRecettesLiees(RecetteImpl.convertToRecetteList(typeRelationDto.getRecettesLiees()));
        }
        if(typeRelationDto.getRecettesLieesA() != null) {
            typeRelation.setRecettesLieesA(RecetteImpl.convertToRecetteList(typeRelationDto.getRecettesLieesA()));
        }
        return typeRelation;
    }
    public static List<TypeRelationDto> convertToTypeRelationDtoList(List<TypeRelation> typeRelations) {
        List<TypeRelationDto> typeRelationDtos = new ArrayList<>();
        for (TypeRelation typeRelation : typeRelations) {
            typeRelationDtos.add(convertToTypeRelationDto(typeRelation));
        }
        return typeRelationDtos;
    }

    public static List<TypeRelation> convertToTypeRelationList(List<TypeRelationDto> typeRelationDtos) {
        List<TypeRelation> typeRelations = new ArrayList<>();
        for (TypeRelationDto typeRelationDto : typeRelationDtos) {
            typeRelations.add(convertToTypeRelation(typeRelationDto));
        }
        return typeRelations;
    }
}
