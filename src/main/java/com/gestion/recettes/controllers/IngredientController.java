package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.IngredientDto;
import com.gestion.recettes.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/ingredients")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @PostMapping(path = "/creer")
    public IngredientDto creer(@RequestBody IngredientDto ingredientDTO){
        return ingredientService.creer(ingredientDTO);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<IngredientDto> lire(@PathVariable("id") Long id){
        IngredientDto ingredientDTO = ingredientService.lire(id);
        if(ingredientDTO != null){
            return ResponseEntity.ok(ingredientDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<IngredientDto> getAllIngredients()
    {
        return ingredientService.lireTous();
    }

    @PutMapping(path = "/modifier/{id}")
    public IngredientDto modifier(@PathVariable("id") Long id, @RequestBody IngredientDto ingredientDTO)
    {
        return ingredientService.modifier(id, ingredientDTO);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimee = ingredientService.supprimer(id);
        return supprimee;
    }
}
