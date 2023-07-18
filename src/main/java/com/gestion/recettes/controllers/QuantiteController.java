package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.*;
import com.gestion.recettes.service.QuantiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/quantites")
public class QuantiteController {

    @Autowired
    private QuantiteService quantiteService;

    @PostMapping(path = "/creer")
    public QuantiteDto creer(@RequestBody QuantiteDto quantiteDto)
    {
        return quantiteService.creer(quantiteDto);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<QuantiteDto> lire(@PathVariable("id") Long id) {
        QuantiteDto quantiteDto = quantiteService.lire(id);
        if(quantiteDto != null) {
            return ResponseEntity.ok(quantiteDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(path="/lireTous")
    public List<QuantiteDto> lireTous()
    {
        List<QuantiteDto> toutesQuantites = quantiteService.lireTous();
        return toutesQuantites;
    }

    @PutMapping(path = "/modifier/{id}")
    public QuantiteDto modifier(@PathVariable("id") Long id,@RequestBody QuantiteDto quantiteDto)
    {
       return quantiteService.modifier(id, quantiteDto);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean deleteQuantity(@PathVariable (value="id") Long id)
    {
        Boolean deleteQuantity = quantiteService.supprimer(id);
        return deleteQuantity;
    }
}
