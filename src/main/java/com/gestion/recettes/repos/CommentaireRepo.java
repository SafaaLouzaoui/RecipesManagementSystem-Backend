package com.gestion.recettes.repos;

import com.gestion.recettes.entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepo extends JpaRepository<Commentaire, Long> {

    List<Commentaire> findByRecetteId(Long idRecette);
}
