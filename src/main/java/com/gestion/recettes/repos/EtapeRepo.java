package com.gestion.recettes.repos;

import com.gestion.recettes.entities.Etape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapeRepo extends JpaRepository<Etape, Long> {
}
