package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.EtapeDto;
import com.gestion.recettes.service.EtapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/etapes")
public class EtapeController {
    @Autowired
    private EtapeService etapeService;

    @PostMapping(path = "/creer")
    public EtapeDto creer(@RequestBody EtapeDto etapeDTO){
        return etapeService.creer(etapeDTO);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<EtapeDto> lire(@PathVariable("id") Long id){
        EtapeDto etapeDTO = etapeService.lire(id);
        if(etapeDTO != null){
            return ResponseEntity.ok(etapeDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<EtapeDto> getAllSteps()
    {
        List<EtapeDto> etapeDtos = etapeService.lireTous();
        return etapeDtos;
    }

    @PutMapping(path = "/modifier/{id}")
    public EtapeDto modifier(@PathVariable("id") Long id, @RequestBody EtapeDto etapeDTO)
    {
        return etapeService.modifier(id, etapeDTO);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimee = etapeService.supprimer(id);
        return supprimee;
    }
}
