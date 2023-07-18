package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.IngredientDto;
import com.gestion.recettes.entities.Ingredient;
import com.gestion.recettes.entities.Quantite;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.IngredientRepo;
import com.gestion.recettes.repos.QuantiteRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.IngredientService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientImpl implements IngredientService {
    private final IngredientRepo ingredientRepo;
    private static IngredientRepo ingredientRepoStat;
    private final RecetteRepo recetteRepo;
    private static RecetteRepo recetteRepoStat;
    private final QuantiteRepo quantiteRepo;
    private static QuantiteRepo quantiteRepoStat;
    private final JdbcTemplate jdbcTemplate;

    public IngredientImpl(IngredientRepo ingredientRepo, RecetteRepo recetteRepo, QuantiteRepo quantiteRepo, JdbcTemplate jdbcTemplate) {
        this.ingredientRepo = ingredientRepo;
        this.recetteRepo = recetteRepo;
        this.quantiteRepo = quantiteRepo;
        this.jdbcTemplate = jdbcTemplate;
        ingredientRepoStat = ingredientRepo;
        recetteRepoStat = recetteRepo;
        quantiteRepoStat = quantiteRepo;
    }


    @Override
    public IngredientDto creer(IngredientDto ingredientDTO) {

//         Ingredient ingredient = convertToIngredient(ingredientDTO);
//         ingredientRepo.save(ingredient);
//         return convertToIngredientDTO(ingredient);
        if (ingredientDTO.getId() != null) {
            // Check if the ingredient with the given ID exists
            Optional<Ingredient> optionalIngredient = ingredientRepo.findById(ingredientDTO.getId());
            if (optionalIngredient.isPresent()) {
                // Ingredient already exists, return the existing ingredient
                Ingredient existingIngredient = optionalIngredient.get();
                return convertToIngredientDTO(existingIngredient);
            }
        }

        // Ingredient doesn't exist, create a new ingredient
        Ingredient ingredient = convertToIngredient(ingredientDTO);
        ingredientRepo.save(ingredient);
        return convertToIngredientDTO(ingredient);
    }

    @Override
    public List<IngredientDto> lireTous() {
        List<Ingredient> ingredients = ingredientRepo.findAll();
        return convertToIngredientDtoList(ingredients);
    }


    @Override
    public IngredientDto lire(Long id) {
        Optional<Ingredient> optionalIngredient = ingredientRepo.findById(id);

        if(optionalIngredient.isPresent()){
            Ingredient ingredient = optionalIngredient.get();
            return convertToIngredientDTO(ingredient);
        }else{
            System.out.println("Cet ingrédient n'existe pas");
            return null;
        }
    }

    @Override
    public IngredientDto modifier(Long id, IngredientDto ingredientDTO) {
        Optional<Ingredient> optionalIngredient = ingredientRepo.findById(id);

        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();

            if (ingredientDTO.getNom() != null) {
                ingredient.setNom(ingredientDTO.getNom());
            }
            if (ingredientDTO.getDescription() != null) {
                ingredient.setDescription(ingredientDTO.getDescription());
            }
            if (ingredientDTO.getMedia() != null) {
                ingredient.setMedia(ingredientDTO.getMedia());
            }

            if (ingredientDTO.getRecettesIds() != null) {
                List<Recette> recettes = new ArrayList<>();
                boolean validRecettes = true;

                for (Long recetteId : ingredientDTO.getRecettesIds()) {
                    Optional<Recette> optionalRecette = recetteRepo.findById(recetteId);

                    if (optionalRecette.isPresent()) {
                        recettes.add(optionalRecette.get());
                    } else {
                        validRecettes = false;
                    }
                }

                if (validRecettes) {
                    ingredient.setRecettes(recettes);
                }
            }

            if (ingredientDTO.getIngredientIdReference() != null) {
                Optional<Ingredient> optionalIngredientReference = ingredientRepo.findById(ingredientDTO.getIngredientIdReference());

                if (optionalIngredientReference.isPresent()) {
                    ingredient.setIngredientReference(optionalIngredientReference.get());
                }
            }

            if (ingredientDTO.getQuantitesIds() != null) {
                List<Quantite> quantites = new ArrayList<>();
                boolean validQuantites = true;

                for (Long quantiteId : ingredientDTO.getQuantitesIds()) {
                    Optional<Quantite> optionalQuantite = quantiteRepo.findById(quantiteId);

                    if (optionalQuantite.isPresent()) {
                        quantites.add(optionalQuantite.get());
                    } else {
                        validQuantites = false;
                    }
                }

                if (validQuantites) {
                    ingredient.setQuantites(quantites);
                }
            }

            Ingredient updatedIngredient = ingredientRepo.save(ingredient);
            return convertToIngredientDTO(updatedIngredient);
        } else {
            System.out.println("Cet ingrédient n'existe pas");
        }

        return ingredientDTO;
    }


    @Override
    public Boolean supprimer(Long id) {
        if(ingredientRepo.existsById(id)){
            deleteRecetteIngredientsByIngredientId(id);
            ingredientRepo.deleteById(id);
        }else{
            System.out.println("Cet ingrédient n'existe pas");
        }
        return true;
    }

    public void deleteRecetteIngredientsByIngredientId(Long id) {
        String sqlRecetteIngredients = "DELETE FROM recette_ingredients WHERE ingredients_id = ?";
        jdbcTemplate.update(sqlRecetteIngredients, id);

        String sqlQuantite = "DELETE FROM quantite WHERE ingredient_id = ?";
        jdbcTemplate.update(sqlQuantite, id);
    }


    public static IngredientDto convertToIngredientDTO(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        IngredientDto ingredientDTO = new IngredientDto();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setNom(ingredient.getNom());
        ingredientDTO.setDescription(ingredient.getDescription());
        ingredientDTO.setMedia(ingredient.getMedia());
        /*if(ingredient.getRecettes() != null) {
            ingredientDTO.setRecettes(RecetteImpl.convertToRecetteDtoList(ingredient.getRecettes()));
        }*/

        // Convertir l'ingrédient de référence
        if (ingredient.getIngredientReference() != null) {
            IngredientDto ingredientReferenceDto = new IngredientDto();
            ingredientReferenceDto.setId(ingredient.getIngredientReference().getId());
            ingredientReferenceDto.setNom(ingredient.getIngredientReference().getNom());
            ingredientReferenceDto.setDescription(ingredient.getDescription());
            ingredientDTO.setIngredientIdReference(ingredient.getIngredientReference().getId());
        }

        if (ingredient.getQuantites() != null) {
            List<Long> quantites = new ArrayList<>();
            for (Quantite quantite : ingredient.getQuantites()){
                quantites.add(quantite.getId());
            }
            ingredientDTO.setQuantitesIds((quantites));
        }
        return ingredientDTO;
    }

    public static Ingredient convertToIngredient(IngredientDto ingredientDTO) {
        if (ingredientDTO != null) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientDTO.getId());
            ingredient.setNom(ingredientDTO.getNom());
            ingredient.setDescription(ingredientDTO.getDescription());
            ingredient.setMedia(ingredientDTO.getMedia());
            if(ingredientDTO.getRecettesIds() != null) {
                List<Recette> recettes = new ArrayList<>();
                boolean valid = true;
                for (Long idIng : ingredientDTO.getRecettesIds()){
                    if (recetteRepoStat.existsById(idIng)) {
                        recettes.add(recetteRepoStat.getReferenceById(idIng));
                    }else
                        valid = false;
                }
                if (valid)
                    ingredient.setRecettes(recettes);
            }
            if (ingredientDTO.getIngredientIdReference() != null) {
                if (ingredientRepoStat.existsById(ingredientDTO.getIngredientIdReference())){
                    Ingredient ingredient1 = ingredientRepoStat.getReferenceById(ingredientDTO.getIngredientIdReference());
                    ingredient.setIngredientReference(ingredient1);
                }

            }if (ingredientDTO.getQuantitesIds() != null) {
                List<Quantite> quantites = new ArrayList<>();
                boolean valid = true;
                for (Long idQun : ingredientDTO.getQuantitesIds()){
                    if (quantiteRepoStat.existsById(idQun)) {
                        quantites.add(quantiteRepoStat.getReferenceById(idQun));
                    }else
                        valid = false;
                }
                if (valid)
                    ingredient.setQuantites(quantites);

            }
            return ingredient;
        } else {
            return null;
        }
    }
    public static List<IngredientDto> convertToIngredientDtoList(List<Ingredient> ingredients) {
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientDtos.add(convertToIngredientDTO(ingredient));
        }
        return ingredientDtos;
    }

    public static List<Ingredient> convertToIngredientList(List<IngredientDto> ingredientDtos) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientDto ingredientDTO : ingredientDtos) {
            ingredients.add(convertToIngredient(ingredientDTO));
        }
        return ingredients;
    }
}
