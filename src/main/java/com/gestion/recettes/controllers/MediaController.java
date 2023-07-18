package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.MediaDto;
import com.gestion.recettes.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("api/v1/medias")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping(path = "/creer")
    public MediaDto creer(@RequestBody MediaDto mediaDTO){
        return mediaService.creer(mediaDTO);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<MediaDto> lire(@PathVariable("id") Long id){
        MediaDto mediaDTO = mediaService.lire(id);
        if(mediaDTO != null){
            return ResponseEntity.ok(mediaDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<MediaDto> getAllMedias()
    {
        List<MediaDto> mediaDtos = mediaService.lireTous();
        return mediaDtos;
    }

    @PutMapping(path = "/modifier/{id}")
    public MediaDto modifier(@PathVariable("id") Long id, @RequestBody MediaDto mediaDTO)
    {
        return mediaService.modifier(id, mediaDTO);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimee = mediaService.supprimer(id);
        return supprimee;
    }
}
