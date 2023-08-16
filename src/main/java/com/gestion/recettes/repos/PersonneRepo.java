
package com.gestion.recettes.repos;
import com.gestion.recettes.entities.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface PersonneRepo extends JpaRepository<Personne, Long> {

    //Personne findByAdresseMail(String adresseMail);
    Personne findByAdresseMailAndStatutNot(String adresseMail, String statut);
    Optional<Personne> findByAdresseMailAndMotDePasse(String adresseMail, String motDePasse);
    Optional<Personne> findByAdresseMail(String adresseMail);

}

