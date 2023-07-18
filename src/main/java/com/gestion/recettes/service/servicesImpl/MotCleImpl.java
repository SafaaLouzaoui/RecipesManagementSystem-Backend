package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.MotCleDto;
import com.gestion.recettes.entities.MotCle;
import com.gestion.recettes.entities.Recette;
import com.gestion.recettes.repos.MotCleRepo;
import com.gestion.recettes.repos.RecetteRepo;
import com.gestion.recettes.service.MotCleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MotCleImpl implements MotCleService {
    @Autowired
    private MotCleRepo motCleRepo;
    private final RecetteRepo recetteRepo;
    private static RecetteRepo recetteRepoStat;

    public MotCleImpl(RecetteRepo recetteRepo) {
        this.recetteRepo = recetteRepo;
        recetteRepoStat = recetteRepo;
    }


    @Override
    public MotCleDto creer(MotCleDto motCleDto) {
       MotCle motCle = convertToMotCle(motCleDto);
       motCleRepo.save(motCle);
       return convertToMotCleDto(motCle);
    }

    @Override
    public MotCleDto lire(Long id) {
        Optional<MotCle> optionalMotCle = motCleRepo.findById(id);
        if(optionalMotCle.isPresent()){
            MotCle motCle = optionalMotCle.get();
          return convertToMotCleDto(motCle);
        }else{
            System.out.println("Ce mot clé n'existe pas");
            return null;
        }
    }
    @Override
    public List<MotCleDto> lireTous() {

        List<MotCle> getMotsCles = motCleRepo.findAll();
        return convertToMotCleDtoList(getMotsCles);
    }

    @Override
    public MotCleDto modifier(Long id, MotCleDto motCleDto) {
        if(motCleRepo.existsById(id)){
            MotCle motCle = motCleRepo.getById(id);
            if (motCleDto.getMot() != null)
                motCle.setMot(motCleDto.getMot());
            if(motCleDto.getRecetteIds() != null) {
                List<Recette> recettes = new ArrayList<>();
                for (Long recetId : motCleDto.getRecetteIds()){
                    if (recetteRepo.existsById(recetId)){
                        recettes.add(recetteRepo.getReferenceById(recetId));
                    }
                }
                motCle.setRecettes(recettes);
            }
            MotCle updatedMotCle = motCleRepo.save(motCle);
            return convertToMotCleDto(updatedMotCle);
        }else{
            System.out.println("Ce mot clé n'existe pas");
        }
        return motCleDto;
    }

    @Override
    public Boolean supprimer(Long id) {
        if(motCleRepo.existsById(id))
        {
            motCleRepo.deleteById(id);
        }else{
            System.out.println("Ce mot clé n'existe pas");
        }
        return true;
    }


    public static MotCleDto convertToMotCleDto(MotCle motCle) {
        MotCleDto motCleDto = new MotCleDto();
        motCleDto.setId(motCle.getId());
        motCleDto.setMot(motCle.getMot());
        if(motCle.getRecettes() != null) {
            List<Long> ids = new ArrayList<>();
            for (Recette recette : motCle.getRecettes())
                ids.add(recette.getId());
            motCleDto.setRecetteIds(ids);
        }
        return motCleDto;
    }


    public static MotCle convertToMotCle(MotCleDto motCleDto) {
        MotCle motCle = new MotCle();
        motCle.setId(motCleDto.getId());
        motCle.setMot(motCleDto.getMot());
        if(motCleDto.getRecetteIds() != null) {
            List<Recette> recettes = new ArrayList<>();
            for (Long recetId : motCleDto.getRecetteIds()){
                if (recetteRepoStat.existsById(recetId)){
                    recettes.add(recetteRepoStat.getReferenceById(recetId));
                }
            }
            motCle.setRecettes(recettes);
        }
        return motCle;
    }

    public static List<MotCleDto> convertToMotCleDtoList(List<MotCle> motCles) {
        List<MotCleDto> motCleDtos = new ArrayList<>();
        for (MotCle motCle : motCles) {
            motCleDtos.add(convertToMotCleDto(motCle));
        }
        return motCleDtos;
    }

    public static List<MotCle> convertToMotCleList(List<MotCleDto> motCleDtos) {
        List<MotCle> motCles = new ArrayList<>();
        for (MotCleDto motCleDto : motCleDtos) {
            motCles.add(convertToMotCle(motCleDto));
        }
        return motCles;
    }
}
