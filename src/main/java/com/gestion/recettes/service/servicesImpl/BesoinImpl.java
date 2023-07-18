package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.BesoinDto;
import com.gestion.recettes.entities.Besoin;
import com.gestion.recettes.repos.BesoinRepo;
import com.gestion.recettes.service.BesoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BesoinImpl implements BesoinService {
    private final BesoinRepo besoinRepo;

    @Autowired
    public BesoinImpl(BesoinRepo besoinRepo) {
        this.besoinRepo = besoinRepo;
    }

    @Override
    public BesoinDto creer(BesoinDto besoinDTO) {
        Besoin besoin = convertToBesoin(besoinDTO);
        besoin = besoinRepo.save(besoin);
        return convertToBesoinDTO(besoin);
    }

    @Override
    public List<BesoinDto> lireTous() {
        List<Besoin> besoins = besoinRepo.findAll();
        return convertToBesoinDTOList(besoins);
    }

    @Override
    public BesoinDto lire(Long id) {
        Optional<Besoin> optionalBesoin = besoinRepo.findById(id);
        if (optionalBesoin.isPresent()) {
            Besoin besoin = optionalBesoin.get();
            return convertToBesoinDTO(besoin);
        } else {
            System.out.println("Ce Besoin n'existe pas");
            return null;
        }
    }

    @Override
    public BesoinDto modifier(Long id, BesoinDto besoinDTO) {
        Optional<Besoin> optionalBesoin = besoinRepo.findById(id);
        if (optionalBesoin.isPresent()) {
            Besoin besoin = optionalBesoin.get();
            if (besoinDTO.getDescription() != null)
                besoin.setDescription(besoinDTO.getDescription());
            if (besoinDTO.getType() != null)
                besoin.setType(besoinDTO.getType());
            if (besoinDTO.getNom() != null)
                besoin.setNom(besoinDTO.getNom());
            if (besoinDTO.getMedia() != null)
                besoin.setMedia(besoinDTO.getMedia());
            besoin = besoinRepo.save(besoin);
            return convertToBesoinDTO(besoin);
        } else {
            throw new RuntimeException("Besoin non trouvé !");
        }
    }

    @Override
    public Boolean supprimer(Long id) {
        if (besoinRepo.existsById(id)) {
            besoinRepo.deleteById(id);
        } else {
            System.out.println("Ce type de relation n'existe pas");
        }
        return true;
    }

    public static BesoinDto convertToBesoinDTO(Besoin besoin) {
        if(besoin != null){
        BesoinDto besoinDTO = new BesoinDto();
        besoinDTO.setId(besoin.getId());
        besoinDTO.setNom(besoin.getNom());
        besoinDTO.setType(besoin.getType());
        besoinDTO.setDescription(besoin.getDescription());
        besoinDTO.setMedia(besoin.getMedia());
        return besoinDTO;
        }
        else{
            return null;
        }
    }

    public static Besoin convertToBesoin(BesoinDto besoinDTO) {
        if(besoinDTO != null) {
            Besoin besoin = new Besoin();
            besoin.setId(besoinDTO.getId());
            besoin.setNom(besoinDTO.getNom());
            besoin.setType(besoinDTO.getType());
            besoin.setDescription(besoinDTO.getDescription());
            besoin.setMedia(besoinDTO.getMedia());
            return besoin;
        }else{
            return null;
        }
    }

    public static List<BesoinDto> convertToBesoinDTOList(List<Besoin> besoinList) {
        List<BesoinDto> besoinDtoList = new ArrayList<>();
        for (Besoin besoin : besoinList) {
            besoinDtoList.add(convertToBesoinDTO(besoin));
        }
        return besoinDtoList;
    }

    public static List<Besoin> convertToBesoinList(List<BesoinDto> besoinDtoList) {
        List<Besoin> besoinList = new ArrayList<>();
        for (BesoinDto besoinDTO : besoinDtoList) {
            besoinList.add(convertToBesoin(besoinDTO));
        }
        return besoinList;
    }
}
