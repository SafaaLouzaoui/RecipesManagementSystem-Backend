package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.BesoinDto;
import com.gestion.recettes.service.BesoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("api/v1/besoins")
public class BesoinController {
    @Autowired
    private BesoinService besoinService;

    @PostMapping(path = "/creer")
    public BesoinDto creer(@RequestBody BesoinDto besoinDTO){
        return besoinService.creer(besoinDTO);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<BesoinDto> lire(@PathVariable("id") Long id){
        BesoinDto besoinDTO = besoinService.lire(id);
        if(besoinDTO != null){
            return ResponseEntity.ok(besoinDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public  List<BesoinDto> getAllBesoins()
    {
        List<BesoinDto> besoinDtoList = besoinService.lireTous();
        return besoinDtoList;
    }

    @PutMapping(path = "modifier/{id}")
    public BesoinDto modifier(@PathVariable("id") Long id, @RequestBody BesoinDto besoinDTO)
    {
        return besoinService.modifier(id, besoinDTO);
    }

    @DeleteMapping(path = "supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimee = besoinService.supprimer(id);
        return supprimee;
    }
}
