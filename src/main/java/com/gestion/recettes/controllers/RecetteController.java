package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.RecetteDto;
import com.gestion.recettes.service.RecetteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/recettes")
public class RecetteController {

    @Autowired
    private RecetteService recetteService;

    @PostMapping(path = "/creer")
    public ResponseEntity<RecetteDto> creer(@RequestBody RecetteDto recetteDto, HttpSession session) {
        RecetteDto createdRecette = recetteService.creer(recetteDto, session);
        if (createdRecette != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecette);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<RecetteDto> lire(@PathVariable("id") Long id) {
        RecetteDto recetteDto = recetteService.lire(id);
        if(recetteDto != null) {
            return ResponseEntity.ok(recetteDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/recettesByCategorie/{id}")
    public List<RecetteDto> recettesByCategorie(@PathVariable Long id) {
        return recetteService.recettesByCategorie(id);
    }


    @GetMapping(path = "/lireTous")
    public List<RecetteDto> lireTous()
    {
        return recetteService.lireTous();
    }

    @PutMapping(path = {"/modifier/{id}", "/mien/modifier/{id}"})
    public RecetteDto modifier(@PathVariable (value = "id") Long id,@RequestBody RecetteDto recetteDto)
    {
        return recetteService.modifier(id, recetteDto);
    }
    
    @DeleteMapping(path = { "/mien/supprimer/{id}", "/supprimer/{id}" })
    public Boolean supprimer(@PathVariable(value = "id") Long id) {
        Boolean recetteSupprimee = recetteService.supprimer(id);
        return recetteSupprimee;
    }
    
    
    @GetMapping("/search")
    public List<RecetteDto> searchRecettesByNom(@RequestParam("nom") String nom) {
        return recetteService.searchRecettesByNom(nom);
    }

    @GetMapping(path = "/mesRecettes/{id}")
    public List<RecetteDto> mesRecette(@PathVariable (value = "id") Long id) {
        return recetteService.mesRecettes(id);
    }

    @GetMapping(path = "/recettes/recetteSiganlees")
    public List<RecetteDto> recetteSignalees(){
        return recetteService.recettesSignalees();
    }

}
