package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.MediaTypeDto;
import com.gestion.recettes.service.MediaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/mediaTypes")
public class MediaTypeController {

    @Autowired
    private MediaTypeService mediaTypeService;

    @PostMapping(path = "/creer")
    public MediaTypeDto creer(@RequestBody MediaTypeDto mediaTypeDto){
        return  mediaTypeService.creer(mediaTypeDto);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<MediaTypeDto> lire(@PathVariable("id") Long id) {
        MediaTypeDto mediaTypeDto = mediaTypeService.lire(id);
        if(mediaTypeDto != null) {
            return ResponseEntity.ok(mediaTypeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<MediaTypeDto> lireTous()
    {
        List<MediaTypeDto> typeDtos = mediaTypeService.lireTous();
        return typeDtos;
    }

    @PutMapping(path = "/modifier/{id}")
    public MediaTypeDto modifier(@PathVariable("id") Long id, @RequestBody MediaTypeDto mediaTypeDto) {
        return mediaTypeService.modifer(id, mediaTypeDto);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimerTypeMedia = mediaTypeService.supprimer(id);
        return supprimerTypeMedia;
    }

}
