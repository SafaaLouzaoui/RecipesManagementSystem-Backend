package com.gestion.recettes.repos;

import com.gestion.recettes.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
    Optional<Profile> findByCode(int codeProfilUtilisateur);
}
