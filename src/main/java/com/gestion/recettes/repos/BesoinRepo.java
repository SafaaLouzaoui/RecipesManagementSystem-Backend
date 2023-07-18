package com.gestion.recettes.repos;

import com.gestion.recettes.entities.Besoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BesoinRepo extends JpaRepository<Besoin, Long> {
}
