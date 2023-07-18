package com.gestion.recettes.repos;

import com.gestion.recettes.entities.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface MediaTypeRepo extends JpaRepository<MediaType, Long> {

}
