package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.CategorieDto;
import com.gestion.recettes.entities.Categorie;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.CategorieRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.CategorieService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategorieImpl implements CategorieService {
    private final CategorieRepo categorieRepo;
    private final RecetteRepo recetteRepo;
    private static RecetteRepo recetteRepoStat;
    private final JdbcTemplate jdbcTemplate;

    public CategorieImpl(RecetteRepo recetteRepo, JdbcTemplate jdbcTemplate, CategorieRepo categorieRepo) {
        this.recetteRepo = recetteRepo;
        this.jdbcTemplate = jdbcTemplate;
        this.categorieRepo = categorieRepo;
        recetteRepoStat = recetteRepo;
    }

    @Override
    public CategorieDto creer(CategorieDto categorieDTO) {
       Categorie categorie = convertToCategorie(categorieDTO);
       categorieRepo.save(categorie);
       return convertToCategorieDTO(categorie);
    }

    @Override
    public List<CategorieDto> lireTous() {

         List<Categorie> categories = categorieRepo.findAll();
         return convertToCategorieDtoList(categories);
    }

    @Override
    public CategorieDto lire(Long id) {
        Optional<Categorie> optionalCategorie = categorieRepo.findById(id);

        if(optionalCategorie.isPresent()){
            Categorie categorie = optionalCategorie.get();
            return convertToCategorieDTO(categorie);
        }else{
            System.out.println("Cette categorie n'existe pas");
            return null;
        }
    }

    @Override
    public CategorieDto modifier(Long id, CategorieDto categorieDTO) {
        Optional<Categorie> optionalCategorie = categorieRepo.findById(id);
        if (optionalCategorie.isPresent()) {
            Categorie categorie = optionalCategorie.get();
            if (categorieDTO.getNomCat() != null) {
                categorie.setNomCat(categorieDTO.getNomCat());
            }
            if (categorieDTO.getDescriptionCat() != null) {
                categorie.setDescriptionCat(categorieDTO.getDescriptionCat());
            }
            if (categorieDTO.getMedia() != null) {
                categorie.setMedia(categorieDTO.getMedia());
            }
            if (categorieDTO.getRecetteIds() != null) {
                List<Recette> recettes = new ArrayList<>();
                for (Long recetteId : categorieDTO.getRecetteIds()) {
                    Optional<Recette> optionalRecette = recetteRepo.findById(recetteId);
                    optionalRecette.ifPresent(recettes::add);
                }
                categorie.setRecettes(recettes);
            }
            Categorie updatedCategorie = categorieRepo.save(categorie);
            return convertToCategorieDTO(updatedCategorie);
        } else {
            System.out.println("Cette categorie n'existe pas");
        }
        return categorieDTO;
    }


    @Override
    public Boolean supprimer(Long id) {
        if(categorieRepo.existsById(id))
        {
            deleteRecetteCategoriesByCategorieId(id);
            categorieRepo.deleteById(id);
        }else {
            System.out.println("Cette categorie n'existe pas");
        }
        return true;
    }

    public static CategorieDto convertToCategorieDTO(Categorie categorie) {
        CategorieDto categorieDTO = new CategorieDto();
        categorieDTO.setIdCat(categorie.getIdCat());
        categorieDTO.setNomCat(categorie.getNomCat());
        categorieDTO.setDescriptionCat(categorie.getDescriptionCat());
        categorieDTO.setMedia(categorie.getMedia());
        if(categorie.getRecettes() != null) {
            List<Long> ids = new ArrayList<>();
            for (Recette recette : categorie.getRecettes())
                ids.add(recette.getId());
            categorieDTO.setRecetteIds(ids);
        }
        return categorieDTO;
    }

    public void deleteRecetteCategoriesByCategorieId(Long idCategorie) {
        String sql = "DELETE FROM recette_categories WHERE categories_id_cat = ?";
        jdbcTemplate.update(sql, idCategorie);
    }

    public static Categorie convertToCategorie(CategorieDto categorieDTO) {
        Categorie categorie = new Categorie();
        categorie.setIdCat(categorieDTO.getIdCat());
        categorie.setNomCat(categorieDTO.getNomCat());
        categorie.setDescriptionCat(categorieDTO.getDescriptionCat());
        categorie.setMedia(categorieDTO.getMedia());
        if(categorieDTO.getRecetteIds() != null) {
            List<Recette> recettes = new ArrayList<>();
            for (Long recetId : categorieDTO.getRecetteIds()){
                if (recetteRepoStat.existsById(recetId)){
                    recettes.add(recetteRepoStat.getReferenceById(recetId));
                }
            }
            categorie.setRecettes(recettes);
        }
        return categorie;
    }
    public static List<CategorieDto> convertToCategorieDtoList(List<Categorie> categories) {
        List<CategorieDto> categorieDtos = new ArrayList<>();
        for (Categorie categorie : categories) {
            categorieDtos.add(convertToCategorieDTO(categorie));
        }
        return categorieDtos;
    }

    public static List<Categorie> convertToCategorieList(List<CategorieDto> categorieDtos) {
        List<Categorie> categories = new ArrayList<>();
        for (CategorieDto categorieDTO : categorieDtos) {
            categories.add(convertToCategorie(categorieDTO));
        }
        return categories;
    }
}
