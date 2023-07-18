package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.EtapeDto;
import com.gestion.recettes.entities.Etape;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.EtapeRepo;
import com.gestion.recettes.repos.MediaRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.EtapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EtapeImpl implements EtapeService {
    @Autowired
    private EtapeRepo etapeRepo;
    private final RecetteRepo recetteRepo;
    private static RecetteRepo recetteRepoStat;
    private final MediaRepo mediaRepo;
    private static MediaRepo mediaRepoStat;

    public EtapeImpl(RecetteRepo recetteRepo, MediaRepo mediaRepo) {
        this.recetteRepo = recetteRepo;
        this.mediaRepo = mediaRepo;

        // Set the static fields after the instance is created
        recetteRepoStat = recetteRepo;
        mediaRepoStat = mediaRepo;
    }


    @Override
    public EtapeDto creer(EtapeDto etapeDTO) {

        Etape etape = convertToEtape(etapeDTO);
        etapeRepo.save(etape);
        return convertToEtapeDTO(etape);
    }

    @Override
    public List<EtapeDto> lireTous() {

        List<Etape> etapes = etapeRepo.findAll();
        return convertToEtapeDtoList(etapes);
    }

    @Override
    public EtapeDto lire(Long id) {

        Optional<Etape> optionalEtape = etapeRepo.findById(id);

        if(optionalEtape.isPresent()){
            Etape etape = optionalEtape.get();
            return convertToEtapeDTO(etape);
        }else{
            System.out.println("Cette étape est introuvable");
            return null;
        }
    }

    @Override
    public EtapeDto modifier(Long id, EtapeDto etapeDTO) {
        if (etapeRepo.existsById(id)) {
            Etape etape = etapeRepo.findById(id).orElse(null);
            if (etape != null) {
                if ((Integer)etapeDTO.getOrdre() != null)
                    etape.setOrdre(etapeDTO.getOrdre());
                if (etapeDTO.getDescription() != null)
                    etape.setDescription(etapeDTO.getDescription());
                if ((Integer)etapeDTO.getDuree() != null)
                    etape.setDuree(etapeDTO.getDuree());
                if (etapeDTO.getMedia() != null)
                    etape.setMedia(etapeDTO.getMedia());
                if (etapeDTO.getRecetteId() != null) {
                    if (recetteRepo.existsById(etapeDTO.getRecetteId())) {
                        Recette recette = recetteRepo.findById(etapeDTO.getRecetteId()).orElse(null);
                        if (recette != null) {
                            etape.setRecette(recette);
                        }
                    }
                }
                Etape updatedEtape = etapeRepo.save(etape);
                return convertToEtapeDTO(updatedEtape);
            }
        } else {
            System.out.println("Cette étape est introuvable");
        }
        return etapeDTO;
    }



    @Override
    public Boolean supprimer(Long id) {

        if(etapeRepo.existsById(id)){
            etapeRepo.deleteById(id);
        }else{
            System.out.println("Cette étape est introuvable");
        }
        return true;
    }

    public static EtapeDto convertToEtapeDTO(Etape etape) {
        EtapeDto etapeDTO = new EtapeDto();
        etapeDTO.setId(etape.getId());
        etapeDTO.setOrdre(etape.getOrdre());
        etapeDTO.setDescription(etape.getDescription());
        etapeDTO.setMedia(etape.getMedia());
        etapeDTO.setDuree(etape.getDuree());

        if (etape.getRecette() != null) {
            etapeDTO.setRecetteId(etape.getRecette().getId());
        }
        return etapeDTO;
    }

    public static Etape convertToEtape(EtapeDto etapeDTO) {
        Etape etape = new Etape();
        etape.setId(etapeDTO.getId());
        etape.setOrdre(etapeDTO.getOrdre());
        etape.setDescription(etapeDTO.getDescription());
        etape.setMedia(etapeDTO.getMedia());
        etape.setDuree(etapeDTO.getDuree());
        if (etapeDTO.getRecetteId() != null) {
            if (recetteRepoStat.existsById(etapeDTO.getRecetteId())){
                Recette recette = recetteRepoStat.getReferenceById(etapeDTO.getRecetteId());
                etape.setRecette(recette);
            }
        }
        return etape;
    }
    public static List<EtapeDto> convertToEtapeDtoList(List<Etape> etapes) {
        List<EtapeDto> etapeDtos = new ArrayList<>();
        for (Etape etape : etapes) {
            etapeDtos.add(convertToEtapeDTO(etape));
        }
        return etapeDtos;
    }

    public static List<Etape> convertToEtapeList(List<EtapeDto> etapeDtos) {
        List<Etape> etapes = new ArrayList<>();
        for (EtapeDto etapeDTO : etapeDtos) {
            etapes.add(convertToEtape(etapeDTO));
        }
        return etapes;
    }
}