package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.MotCleDto;
import com.gestion.recettes.service.MotCleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/motsCles")
public class MotCleController {

    @Autowired
    private MotCleService motCleService;

    @PostMapping(path = "/creer")
    public MotCleDto creer(@RequestBody MotCleDto motCleDto){
        return motCleService.creer(motCleDto);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<MotCleDto> lire(@PathVariable("id") Long id){
        MotCleDto motCleDto = motCleService.lire(id);
        if(motCleDto != null){
            return ResponseEntity.ok(motCleDto);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<MotCleDto> getAllMotsCles()
    {
        List<MotCleDto> motCleDtos = motCleService.lireTous();
        return motCleDtos;
    }

    @PutMapping(path = "/modifier/{id}")
    public MotCleDto modifier(@PathVariable("id") Long id,@RequestBody MotCleDto motCleDto)
    {
        return motCleService.modifier(id, motCleDto);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimee = motCleService.supprimer(id);
        return supprimee;
    }
}
