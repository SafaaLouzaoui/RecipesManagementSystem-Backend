package com.gestion.recettes.repos;

import com.gestion.recettes.entities.Recette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface RecetteRepo extends JpaRepository<Recette, Long> {
}
