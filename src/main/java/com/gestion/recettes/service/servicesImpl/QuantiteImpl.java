package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.*;
import com.gestion.recettes.entities.Ingredient;
import com.gestion.recettes.entities.Quantite;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.IngredientRepo;
import com.gestion.recettes.repos.QuantiteRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.QuantiteService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gestion.recettes.service.servicesImpl.IngredientImpl.convertToIngredient;
import static com.gestion.recettes.service.servicesImpl.RecetteImpl.convertToRecette;

@Service
public class QuantiteImpl implements QuantiteService {
    private final QuantiteRepo quantiteRepo;
    private final IngredientRepo ingredientRepo;
    private static IngredientRepo ingredientRepoStat; // Remove static keyword
    private final RecetteRepo recetteRepo;
    private static RecetteRepo recetteRepoStat; // Remove static keyword

    @Autowired
    public QuantiteImpl(QuantiteRepo quantiteRepo, IngredientRepo ingredientRepo, IngredientRepo ingredientRepoStat, RecetteRepo recetteRepo, RecetteRepo recetteRepoStat) {
        this.quantiteRepo = quantiteRepo;
        this.ingredientRepo = ingredientRepo;
        QuantiteImpl.ingredientRepoStat = ingredientRepoStat;
        this.recetteRepo = recetteRepo;
        QuantiteImpl.recetteRepoStat = recetteRepoStat;
    }


    public QuantiteImpl(QuantiteRepo quantiteRepo, IngredientRepo ingredientRepo, RecetteRepo recetteRepo) {
        this.quantiteRepo = quantiteRepo;
        this.ingredientRepo = ingredientRepo;
        this.recetteRepo = recetteRepo;
    }

    @Override
    public QuantiteDto creer(QuantiteDto quantiteDto){

        Quantite quantite = convertToQuantite(quantiteDto);
        quantiteRepo.save(quantite);
        return convertToQuantiteDto(quantite);

    }

    @Override
    public QuantiteDto lire(Long id){

        Optional<Quantite> optionalQuantite = quantiteRepo.findById(id);

        if(optionalQuantite.isPresent()){
            Quantite quantite = optionalQuantite.get();
            return convertToQuantiteDto(quantite);
        }else{
            System.out.println("Cette quantitee n'existe pas");
            return null;
        }
    }

    @Override
    public List<QuantiteDto> lireTous() {

        List<Quantite> getQuantites = quantiteRepo.findAll();
        return  convertToQuantiteDtoList(getQuantites);
    }

    @Override
    public QuantiteDto modifier(Long id, QuantiteDto quantiteDto) {
        Optional<Quantite> quantiteExistante = quantiteRepo.findById(id);
        if (quantiteExistante.isPresent()) {
            Quantite quantiteModifiee = quantiteExistante.get();

            // Update only if the property is not null
            if (quantiteDto.getUniteDeMesure() != null) {
                quantiteModifiee.setUniteDeMesure(quantiteDto.getUniteDeMesure());
            }
            if (quantiteDto.getNombre() != null) {
                quantiteModifiee.setNombre(quantiteDto.getNombre());
            }

            // Update relations only if the corresponding ID is not null
            if (quantiteDto.getIngredientId() != null) {
                if (ingredientRepo.existsById(quantiteDto.getIngredientId())){
                    Ingredient ingredient = ingredientRepo.getReferenceById(quantiteDto.getIngredientId());
                    quantiteModifiee.setIngredient(ingredient);
                }
            }
            if (quantiteDto.getRecetteId() != null) {
                if (recetteRepo.existsById(quantiteDto.getRecetteId())) {
                    Recette recette = recetteRepo.getReferenceById(quantiteDto.getRecetteId());
                    quantiteModifiee.setRecette(recette);
                }
            }

            quantiteRepo.save(quantiteModifiee);

            return convertToQuantiteDto(quantiteModifiee);
        } else {
            throw new IllegalArgumentException("La quantité spécifiée n'existe pas.");
        }
    }



    @Override
    public Boolean supprimer(Long id) {

        if(quantiteRepo.existsById(id))
        {
            quantiteRepo.deleteById(id);
        }else{
            System.out.println("Cette quantité n'existe pas !!");
        }
        return true;
    }

    public static QuantiteDto convertToQuantiteDto(Quantite quantite) {
        QuantiteDto quantiteDto = new QuantiteDto();
        quantiteDto.setId(quantite.getId());
        quantiteDto.setNombre(quantite.getNombre());
        quantiteDto.setUniteDeMesure(quantite.getUniteDeMesure());
        if(quantite.getIngredient() != null) {
            quantiteDto.setIngredientId(quantite.getIngredient().getId());
        }
        if(quantite.getRecette() != null) {
            quantiteDto.setRecetteId(quantite.getRecette().getId());
        }

        return quantiteDto;
    }

    public static Quantite convertToQuantite(QuantiteDto quantiteDto) {
        Quantite quantite = new Quantite();
        quantite.setId(quantiteDto.getId());
        quantite.setNombre(quantiteDto.getNombre());
        quantite.setUniteDeMesure(quantiteDto.getUniteDeMesure());
        if(quantiteDto.getIngredientId() != null) {
            if (ingredientRepoStat.existsById(quantiteDto.getIngredientId())){
                Ingredient ingredient = ingredientRepoStat.getReferenceById(quantiteDto.getIngredientId());
                quantite.setIngredient(ingredient);
            }
        }
        if(quantiteDto.getRecetteId() != null) {
            if (recetteRepoStat.existsById(quantiteDto.getRecetteId())) {
                Recette recette = recetteRepoStat.getReferenceById(quantiteDto.getRecetteId());
                quantite.setRecette(recette);
            }
        }
        return quantite;
    }

    public static List<QuantiteDto> convertToQuantiteDtoList(List<Quantite> quantites) {
        List<QuantiteDto> quantiteDtos = new ArrayList<>();
        for (Quantite quantite : quantites) {
            quantiteDtos.add(convertToQuantiteDto(quantite));
        }
        return quantiteDtos;
    }

    public static List<Quantite> convertToQuantiteList(List<QuantiteDto> quantiteDtos) {
        List<Quantite> quantites = new ArrayList<>();
        for (QuantiteDto quantiteDto : quantiteDtos) {
            quantites.add(convertToQuantite(quantiteDto));
        }
        return quantites;
    }
}
