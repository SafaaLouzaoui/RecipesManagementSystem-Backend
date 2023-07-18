package com.gestion.recettes.repos;

import com.gestion.recettes.entities.MotCle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface MotCleRepo extends JpaRepository<MotCle, Long> {

}
