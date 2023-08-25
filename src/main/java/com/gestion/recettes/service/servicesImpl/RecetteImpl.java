package com.gestion.recettes.service.servicesImpl;


import com.gestion.recettes.dto.*;
import com.gestion.recettes.entities.*;
import com.gestion.recettes.repos.CategorieRepo;
import com.gestion.recettes.repos.PersonneRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.RecetteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gestion.recettes.service.servicesImpl.BesoinImpl.*;
import static com.gestion.recettes.service.servicesImpl.CategorieImpl.*;
import static com.gestion.recettes.service.servicesImpl.EtapeImpl.*;
import static com.gestion.recettes.service.servicesImpl.IngredientImpl.*;
import static com.gestion.recettes.service.servicesImpl.MediaImpl.*;
import static com.gestion.recettes.service.servicesImpl.MotCleImpl.*;
import static com.gestion.recettes.service.servicesImpl.PersonneImpl.convertToPersonne;
import static com.gestion.recettes.service.servicesImpl.QuantiteImpl.*;

@Service
public class RecetteImpl implements RecetteService {

    private static RecetteRepo recetteRepoStat;
    private final RecetteRepo recetteRepo;
    private final IngredientImpl ingredientImpl;
    private final QuantiteImpl quantiteImpl;
    private final CategorieImpl categorieImpl;
    private final EtapeImpl etapeImpl;
    private final MotCleImpl motCleImpl;
    private final BesoinImpl besoinImpl;
    private final MediaImpl mediaImpl;
    private final CommentaireImpl commentaireImpl;
    private final PersonneRepo personneRepo;
    private final JdbcTemplate jdbcTemplate;
    private final CategorieRepo categorieRepo;

    @Autowired
    public RecetteImpl(RecetteRepo recetteRepo, IngredientImpl ingredientImpl,
                       QuantiteImpl quantiteImpl, CategorieImpl categorieImpl, EtapeImpl etapeImpl,
                       MotCleImpl motCleImpl, BesoinImpl besoinImpl, MediaImpl mediaImpl,
                       CommentaireImpl commentaireImpl, PersonneRepo personneRepo, JdbcTemplate jdbcTemplate, CategorieRepo categorieRepo) {
        this.recetteRepo = recetteRepo;
        this.ingredientImpl = ingredientImpl;
        this.quantiteImpl = quantiteImpl;
        this.categorieImpl = categorieImpl;
        this.etapeImpl = etapeImpl;
        this.motCleImpl = motCleImpl;
        this.besoinImpl = besoinImpl;
        this.mediaImpl = mediaImpl;
        this.commentaireImpl = commentaireImpl;
        this.personneRepo = personneRepo;
        this.jdbcTemplate = jdbcTemplate;
        recetteRepoStat = recetteRepo;
        this.categorieRepo = categorieRepo;
    }

    @Override
    public RecetteDto creer(RecetteDto recetteDto, HttpSession session) {
        Recette recette = convertToRecette(recetteDto);

        List<Ingredient> ingredients = convertToIngredientList(recetteDto.getIngredients());
        List<Categorie> categories = convertToCategorieList(recetteDto.getCategories());
        List<Besoin> besoins = convertToBesoinList(recetteDto.getBesoins());
        List<Media> medias = convertToMediaList(recetteDto.getMedias());
        List<MotCle> motCles = convertToMotCleList(recetteDto.getMotCles());
        List<Etape> etapes = convertToEtapeList(recetteDto.getEtapes());
        List<Quantite> quantites = convertToQuantiteList(recetteDto.getQuantites());
        

        //***ingredientList is the list that will added to the created recette
        List<Ingredient> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == null) {
                IngredientDto createdIngredient = ingredientImpl.creer(convertToIngredientDTO(ingredient));
                ingredient = convertToIngredient(createdIngredient);
            }
            ingredientList.add(ingredient);
        }

        //same change here as ***
        List<Quantite> quantiteList = new ArrayList<>();
        if (quantites.size() == ingredients.size()){
            for (int i = 0; i < quantites.size(); i++) {
                Quantite quantite = quantites.get(i);
                Ingredient ingredient = ingredients.get(i);
                quantite.setIngredient(ingredient);
                QuantiteDto quantiteDto = quantiteImpl.creer(convertToQuantiteDto(quantite));
                quantiteList.add(convertToQuantite(quantiteDto));
            }
        }

        List<Categorie> categorieList = new ArrayList<>();
        for (Categorie categorie : categories) {
            if (categorie.getIdCat() == null){
                CategorieDto categorieDTO = categorieImpl.creer(convertToCategorieDTO(categorie));
                categorie = convertToCategorie(categorieDTO);
            }
            categorieList.add(categorie);
        }

        //same here as ***
        List<Etape> etapeList = new ArrayList<>();
        for (Etape etape : etapes) {
            EtapeDto etapeDto = etapeImpl.creer(convertToEtapeDTO(etape));
            etapeList.add(convertToEtape(etapeDto));
        }

        //Probleme d'ajout le nouveau mot cle verifier pour les autres sont de meme
        List<MotCle> motCleList = new ArrayList<>();
        for (MotCle motCle : motCles) {
            if (motCle.getId() == null){
                MotCleDto motCleDto = motCleImpl.creer(convertToMotCleDto(motCle));
                motCle = convertToMotCle(motCleDto);
            }
            motCleList.add(motCle);
        }

        //same here as ***
        /*List<Besoin> besoinList = new ArrayList<>();
        for (Besoin besoin : besoins) {
            if (besoin.getId() == null){
                BesoinDto besoinDTO = besoinImpl.creer(convertToBesoinDTO(besoin));
                besoin = convertToBesoin(besoinDTO);
            }
            besoinList.add(besoin);
        }*/

        List<Media> mediaList = new ArrayList<>();
        for (Media media : medias) {
            MediaDto mediaDTO = mediaImpl.creer(convertToMediaDTO(media));
            mediaList.add(convertToMedia(mediaDTO));
        }

        recette.setVisibilitee("public");
        recette.setQuantites(quantiteList);
        recette.setIngredients(ingredientList);
        recette.setCategories(categories);
        recette.setEtapes(etapeList);
        recette.setMotCles(motCleList);
        recette.setMedias(mediaList);

        Long userId = (Long) session.getAttribute("userId");
        //Long userId = recetteDto.getUtilisateurCreateur().getId();
        if (userId != null && personneRepo.existsById(userId)){
            Personne personneCre = personneRepo.getReferenceById(userId);
            recette.setUtilisateurCreateur(personneCre);
        }

        recette = recetteRepo.save(recette);

        for (QuantiteDto quantiteDto : convertToQuantiteDtoList(quantiteList)){
            quantiteDto.setRecetteId(recette.getId());
            quantiteImpl.modifier(quantiteDto.getId(), quantiteDto);
        }
        for (EtapeDto etapeDTO : convertToEtapeDtoList(etapeList)){
            etapeDTO.setRecetteId(recette.getId());
            etapeImpl.modifier(etapeDTO.getId(), etapeDTO);
        }
        for (MediaDto mediaDTO : convertToMediaDTOList(mediaList)) {
            mediaDTO.setRecetteId(recette.getId());
            if (mediaDTO.getId() != null)
                mediaImpl.modifier(mediaDTO.getId(), mediaDTO);
            else {
                mediaImpl.creer(mediaDTO);
            }
        }

        return convertToRecetteDto(recette);
    }



    @Override
    public RecetteDto lire(Long id) {
        Optional<Recette> optionalRecette = recetteRepo.findById(id);
        if (optionalRecette.isPresent()) {
            Recette recette = optionalRecette.get();
            //recette.setCommentaires(CommentaireImpl.convertToCommentaireList(commentaireImpl.commentairesRecette(id)));
            return convertToRecetteDto(recette);
        } else {
            System.out.println("Cette recette n'existe pas");
            return null;
        }
    }

    @Override
    public List<RecetteDto> lireTous() {
        List<Recette> recetteList = recetteRepo.findAll();
        List<RecetteDto> recetteDtos = new ArrayList<>();
        for (Recette recette : recetteList) {
            //recette.setCommentaires(CommentaireImpl.convertToCommentaireList(commentaireImpl.commentairesRecette(recette.getId())));
            recetteDtos.add(convertToRecetteDto(recette));
        }
        return recetteDtos;
    }

    @Override
    public RecetteDto modifier(Long id,RecetteDto recetteDto) {

        Optional<Recette> optionalRecette = recetteRepo.findById(id);
        if(optionalRecette.isPresent()){

            Recette recette = optionalRecette.get();
            recette.setNom(recetteDto.getNom());
            recette.setDescription(recetteDto.getDescription());
            recette.setOrigine(recetteDto.getOrigine());
            recette.setDureePreparation(recetteDto.getDureePreparation());
            recette.setDureeCuisson(recetteDto.getDureeCuisson());
            recette.setNbrPersonnes(recetteDto.getNbrPersonnes());
            recette.setVisibilitee(recetteDto.getVisibilitee());
            recette.setStatut(recetteDto.getStatut());
            recette.setDateSoumission(recetteDto.getDateSoumission());
            recette.setDatePublication(recetteDto.getDatePublication());
            recette.setUtilisateurCreateur(convertToPersonne(recetteDto.getUtilisateurCreateur()));
            if (recetteDto.getUtilisateurApprobateur() != null)
                recette.setUtilisateurApprobateur(convertToPersonne(recetteDto.getUtilisateurApprobateur()));
            recette.setSignalants(PersonneImpl.convertToPersonneList(recetteDto.getSignalants()));
            
            List<Media> medias = new ArrayList<>();
            for (Media media : recette.getMedias()){
                mediaImpl.supprimer(media.getId());
            }
            for (MediaDto mediaDTO : recetteDto.getMedias()){
                mediaDTO.setRecetteId(id);
                if (mediaDTO.getId() != null)
                    mediaDTO.setId(null);
                MediaDto mediaDto = mediaImpl.creer(mediaDTO);
                medias.add(convertToMedia(mediaDto));
            }
            recette.setMedias(medias);
            
            /*List<Besoin> besoins = new ArrayList<>();
            for (BesoinDto besoinDTO : recetteDto.getBesoins()){
                if (besoinDTO.getId() != null){
                    besoins.add(convertToBesoin(besoinImpl.modifier(besoinDTO.getId(), besoinDTO)));
                }
                else {
                    besoinDTO = besoinImpl.creer(besoinDTO);
                    besoins.add(convertToBesoin(besoinDTO));
                }
            }
            recette.setBesoins(besoins);*/
            
            recette.setMotCles(convertToMotCleList(recetteDto.getMotCles()));
    
            List<Ingredient> ingredientList = new ArrayList<>();
            List<Quantite> quantitesList = new ArrayList<>();
    
            int index = 0;
    
            for (IngredientDto ingredientDto : recetteDto.getIngredients()) {
                Ingredient ingredient;
        
                if (ingredientDto.getId() != null) {
                    ingredient = convertToIngredient(ingredientImpl.modifier(ingredientDto.getId(), ingredientDto));
                } else {
                    IngredientDto newIngredientDto = ingredientImpl.creer(ingredientDto);
                    ingredient = IngredientImpl.convertToIngredient(newIngredientDto);
                }
        
                ingredientList.add(ingredient);
        
                QuantiteDto quantiteDto = recetteDto.getQuantites().get(index);
                Quantite quantite = convertToQuantite(quantiteDto);
        
                if (quantiteDto.getId() != null) {
                    quantite.setIngredient(ingredient);
                    quantitesList.add(convertToQuantite(quantiteImpl.modifier(quantite.getId(), convertToQuantiteDto(quantite))));
                } else {
                    quantite.setIngredient(ingredient);
                    quantitesList.add(convertToQuantite(quantiteImpl.creer(convertToQuantiteDto(quantite))));
                }
        
                index++;
            }
    
            recette.setIngredients(ingredientList);
            recette.setQuantites(quantitesList);
    
    
            List<Etape> etapes = new ArrayList<>();
            for (EtapeDto etapeDTO : recetteDto.getEtapes()){
                if (etapeDTO.getId() != null)
                    etapes.add(convertToEtape(etapeImpl.modifier(etapeDTO.getId(), etapeDTO)));
                else {
                    etapes.add(convertToEtape(etapeImpl.creer(etapeDTO)));
                }
            }
            recette.setEtapes(etapes);

            recette.setCategories(convertToCategorieList(recetteDto.getCategories()));
            
            if (recetteDto.getTypeRelation() != null)
                recette.setTypeRelation(TypeRelationImpl.convertToTypeRelation(recetteDto.getTypeRelation()));

            if (recetteDto.getTypeRel() != null)
                recette.setTypeRel(TypeRelationImpl.convertToTypeRelation(recetteDto.getTypeRel()));

            /*if(recetteDto.getRecettes() != null) {
                recette.setRecettes(RecetteImpl.convertToRecetteList(recetteDto.getRecettes()));
            }*/
            
            

            Recette updatedRecette = recetteRepo.save(recette);
            return convertToRecetteDto(updatedRecette);

        } else{
           System.out.println("Cette Recette n'existe pas");
        }
        return recetteDto;
    }

    @Override
    public Boolean supprimer(Long id) {
        if(recetteRepo.existsById(id))
        {
            recetteRepo.deleteById(id);
        }else{
            System.out.println("Cette recette n'existe pas");
        }
        return null;
    }

    @Override
    public List<RecetteDto> recettesByCategorie(Long id) {
        String sql = "SELECT r.* FROM recette r " +
                "JOIN recette_categories rc ON r.id = rc.recettes_id " +
                "WHERE rc.categories_id_cat = ?";

        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            // Extract data from the result set and create RecetteDto objects
            Long idRecette = rs.getLong("id");

            Optional<Recette> optionalRecette = recetteRepo.findById(idRecette);
            RecetteDto recetteDto = optionalRecette.map(RecetteImpl::convertToRecetteDto).orElse(null);

            return recetteDto;
        });
    }

    @Override
    public List<RecetteDto> searchRecettesByNom(String nom) {
        String sql = "SELECT * FROM recette WHERE nom LIKE ?";

        String searchValue = "%" + nom + "%";

        return jdbcTemplate.query(sql, new Object[]{searchValue}, (rs, rowNum) -> {
            Long idRecette = rs.getLong("id");

            Optional<Recette> optionalRecette = recetteRepo.findById(idRecette);
            RecetteDto recetteDto = optionalRecette.map(RecetteImpl::convertToRecetteDto).orElse(null);

            return recetteDto;
        });
    }

    @Override
    public List<RecetteDto> mesRecettes(Long id) {
        String sql = "SELECT * FROM recette WHERE utilisateur_createur_id = ?";

        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long idRecette = rs.getLong("id");

            Optional<Recette> optionalRecette = recetteRepo.findById(idRecette);
            RecetteDto recetteDto = optionalRecette.map(RecetteImpl::convertToRecetteDto).orElse(null);

            return recetteDto;
        });
    }

    @Override
    public List<RecetteDto> recettesSignalees() {
        String sql = "SELECT recettes_signalees_id FROM personne_recettes_signalees";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id_recette = rs.getLong("recettes_signalees_id");
            Optional<Recette> optionalRecette = recetteRepo.findById(id_recette);
            if (optionalRecette.isPresent()) {
                Recette recette = optionalRecette.get();
                return convertToRecetteDto(recette);
            } else {
                return null; // Or you can throw an exception or handle the missing recipe case differently
            }
        });
    }




    public static RecetteDto convertToRecetteDto(Recette recette) {
        RecetteDto recetteDto = new RecetteDto();

        recetteDto.setId(recette.getId());
        recetteDto.setNom(recette.getNom());
        recetteDto.setDescription(recette.getDescription());
        recetteDto.setOrigine(recette.getOrigine());
        recetteDto.setDureePreparation(recette.getDureePreparation());
        recetteDto.setDureeCuisson(recette.getDureeCuisson());
        recetteDto.setNbrPersonnes(recette.getNbrPersonnes());
        recetteDto.setVisibilitee(recette.getVisibilitee());
        recetteDto.setStatut(recette.getStatut());
        recetteDto.setDateSoumission(recette.getDateSoumission());
        recetteDto.setDatePublication(recette.getDatePublication());
        if(recette.getUtilisateurCreateur() != null) {
            recetteDto.setUtilisateurCreateur(PersonneImpl.convertToPersonneDTO(recette.getUtilisateurCreateur()));
        }
        if(recette.getUtilisateurApprobateur() != null) {
            recetteDto.setUtilisateurApprobateur(PersonneImpl.convertToPersonneDTO(recette.getUtilisateurApprobateur()));
        }
        if(recette.getSignalants() != null) {
            recetteDto.setSignalants(PersonneImpl.convertToPersonneDTOList(recette.getSignalants()));
        }
        if(recette.getMedias() != null) {
            recetteDto.setMedias(MediaImpl.convertToMediaDTOList(recette.getMedias()));
        }
        if(recette.getBesoins() != null) {
            recetteDto.setBesoins(BesoinImpl.convertToBesoinDTOList(recette.getBesoins()));
        }
        if(recette.getMotCles() != null) {
            recetteDto.setMotCles(MotCleImpl.convertToMotCleDtoList(recette.getMotCles()));
        }
        if(recette.getIngredients() != null) {
            recetteDto.setIngredients(IngredientImpl.convertToIngredientDtoList(recette.getIngredients()));
        }
        if(recette.getEtapes() != null) {
            recetteDto.setEtapes(EtapeImpl.convertToEtapeDtoList(recette.getEtapes()));
        }
        if(recette.getCategories() != null) {
            recetteDto.setCategories(CategorieImpl.convertToCategorieDtoList(recette.getCategories()));
        }
        if(recette.getTypeRelation() != null) {
            recetteDto.setTypeRelation(TypeRelationImpl.convertToTypeRelationDto(recette.getTypeRelation()));
        }
        if(recette.getTypeRel() != null) {
            recetteDto.setTypeRel(TypeRelationImpl.convertToTypeRelationDto(recette.getTypeRel()));
        }
        /*if(recette.getRecettes() != null) {
            recetteDto.setRecettes(convertToRecetteDtoList(recette.getRecettes()));
        }*/
        if (recette.getCommentaires() != null) {
            recetteDto.setCommentaires(CommentaireImpl.convertToCommentaireDtoList(recette.getCommentaires()));
        }

        if(recette.getQuantites() != null) {
            recetteDto.setQuantites(QuantiteImpl.convertToQuantiteDtoList(recette.getQuantites()));
        }
        return recetteDto;
    }

    public static Recette convertToRecette(RecetteDto recetteDto) {
        Recette recette = new Recette();

        recette.setNom(recetteDto.getNom());
        recette.setDescription(recetteDto.getDescription());
        recette.setOrigine(recetteDto.getOrigine());
        recette.setDureePreparation(recetteDto.getDureePreparation());
        recette.setDureeCuisson(recetteDto.getDureeCuisson());
        recette.setNbrPersonnes(recetteDto.getNbrPersonnes());
        recette.setVisibilitee(recetteDto.getVisibilitee());
        recette.setStatut(recetteDto.getStatut());
        recette.setDateSoumission(LocalDate.now());
        recette.setDatePublication(recetteDto.getDatePublication());
        if(recetteDto.getUtilisateurCreateur() != null) {
            recette.setUtilisateurCreateur(convertToPersonne(recetteDto.getUtilisateurCreateur()));
        }
        if(recetteDto.getUtilisateurApprobateur() != null) {
            recette.setUtilisateurApprobateur(convertToPersonne(recetteDto.getUtilisateurApprobateur()));
        }
        if(recetteDto.getSignalants() != null) {
            recette.setSignalants(PersonneImpl.convertToPersonneList(recetteDto.getSignalants()));
        }
        if(recetteDto.getMedias() != null) {
            recette.setMedias(convertToMediaList(recetteDto.getMedias()));
        }
        if(recetteDto.getBesoins() != null) {
            recette.setBesoins(convertToBesoinList(recetteDto.getBesoins()));
        }
        if(recetteDto.getMotCles() != null) {
            recette.setMotCles(convertToMotCleList(recetteDto.getMotCles()));
        }
        if(recetteDto.getIngredients() != null) {
            recette.setIngredients(convertToIngredientList(recetteDto.getIngredients()));
        }
        if(recetteDto.getEtapes() != null) {
            recette.setEtapes(convertToEtapeList(recetteDto.getEtapes()));
        }
        if(recetteDto.getCategories() != null) {
            recette.setCategories(convertToCategorieList(recetteDto.getCategories()));
        }
        if(recetteDto.getTypeRelation() != null) {
            recette.setTypeRelation(TypeRelationImpl.convertToTypeRelation(recetteDto.getTypeRelation()));
        }
        if(recetteDto.getTypeRel() != null) {
            recette.setTypeRel(TypeRelationImpl.convertToTypeRelation(recetteDto.getTypeRel()));
        }
        /*if(recetteDto.getRecettes() != null) {
            recette.setRecettes(convertToRecetteList(recetteDto.getRecettes()));
        }*/
        if (recetteDto.getCommentaires() != null) {
            recette.setCommentaires(CommentaireImpl.convertToCommentaireList(recetteDto.getCommentaires()));
        }
        if(recetteDto.getQuantites() != null) {
            recette.setQuantites(convertToQuantiteList(recetteDto.getQuantites()));
        }
        return recette;
    }

    public static List<RecetteDto> convertToRecetteDtoList(List<Recette> recettes) {
        List<RecetteDto> recetteDtos = new ArrayList<>();
        for (Recette recette : recettes) {
            recetteDtos.add(convertToRecetteDto(recette));
        }

        return recetteDtos;
    }

    public static List<Recette> convertToRecetteList(List<RecetteDto> recetteDtos) {
        List<Recette> recettes = new ArrayList<>();
        if (recetteDtos != null) {
            for (RecetteDto recetteDto : recetteDtos) {
                recettes.add(convertToRecette(recetteDto));
            }
        }

        return recettes;
    }
}