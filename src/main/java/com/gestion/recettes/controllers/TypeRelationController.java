package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.TypeRelationDto;
import com.gestion.recettes.service.TypeRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/typesRelations")
public class TypeRelationController {

    @Autowired
    private TypeRelationService typeRelationService;

    @PostMapping(path = "/creer")
    public TypeRelationDto creer(@RequestBody TypeRelationDto typeRelationDto)
    {
        return typeRelationService.creer(typeRelationDto);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<TypeRelationDto> readMediaType(@PathVariable("id") Long id) {
        TypeRelationDto typeRelationDto = typeRelationService.lire(id);
        if(typeRelationDto != null) {
            return ResponseEntity.ok(typeRelationDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(path = "/lireTous")
    public List<TypeRelationDto> lireTous()
    {
        List<TypeRelationDto> toutType = typeRelationService.lireTous();
        return toutType;
    }

    @PutMapping(path = "/modifier/{id}")
    public TypeRelationDto modifier(@PathVariable (value = "id") Long id, @RequestBody TypeRelationDto typeRelationDto)
    {
        return typeRelationService.modifier(id,typeRelationDto);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean deleteTypeRelation(@PathVariable (value = "id") Long id)
    {
        Boolean typesupprime = typeRelationService.supprimer(id);
        return typesupprime;
    }
}
