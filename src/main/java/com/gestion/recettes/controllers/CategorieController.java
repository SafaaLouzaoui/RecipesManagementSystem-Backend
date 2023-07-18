package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.CategorieDto;
import com.gestion.recettes.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/categories")
public class CategorieController {
    @Autowired
    private CategorieService categorieService;

    @PostMapping(path = "/creer")
    public CategorieDto creer(@RequestBody CategorieDto categorieDTO){
        return categorieService.creer(categorieDTO);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<CategorieDto> lire(@PathVariable("id") Long id){
        CategorieDto categorieDTO = categorieService.lire(id);
        if(categorieDTO != null){
            return ResponseEntity.ok(categorieDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<CategorieDto> getAllCategories()
    {
        List<CategorieDto> categorieDtos = categorieService.lireTous();
        return categorieDtos;
    }

    @PutMapping(path = "/modifier/{id}")
    public CategorieDto modifier(@PathVariable("id") Long id, @RequestBody CategorieDto categorieDTO)
    {
        return categorieService.modifier(id, categorieDTO);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimee = categorieService.supprimer(id);
        return supprimee;
    }
}
