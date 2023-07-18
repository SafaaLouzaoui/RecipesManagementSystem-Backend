package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.CommentaireDto;
import com.gestion.recettes.dto.CommentairePersonneContactDto;
import com.gestion.recettes.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/commentaires")
public class CommentaireController {
    @Autowired
    private CommentaireService commentaireService;

    @PostMapping(path = "/creer")
    public CommentaireDto creer(@RequestBody CommentaireDto commentaireDTO,
                                @RequestParam(value = "idRecette") Long idRecette,
                                @RequestParam(value = "idPersonne") Long idPersonne) {
        return commentaireService.creer(commentaireDTO, idRecette, idPersonne);
    }

    @PostMapping(path = "/contactUs")
    public CommentaireDto contact(@RequestBody CommentairePersonneContactDto commentairePersonneContactDto){
        return commentaireService.contactCommentaire(commentairePersonneContactDto);
    }

    @GetMapping(path = "/lireContacts")
    public CommentairePersonneContactDto contacts(){
        return commentaireService.contactes();
    }


    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<CommentaireDto> lire(@PathVariable("id") Long id){
        CommentaireDto commentaireDTO = commentaireService.lire(id);
        if(commentaireDTO != null){
            return ResponseEntity.ok(commentaireDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<CommentaireDto> getAllComments()
    {
        List<CommentaireDto> commentaireDtos = commentaireService.lireTous();
        return commentaireDtos;
    }

    @PutMapping(path = "/modifier/{id}")
    public CommentaireDto modifier(@PathVariable("id") Long id, @RequestBody CommentaireDto commentaireDTO)
    {
        return commentaireService.modifier(id, commentaireDTO);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean supprimer(@PathVariable (value = "id") Long id)
    {
        Boolean supprimee = commentaireService.supprimer(id);
        return supprimee;
    }
}
