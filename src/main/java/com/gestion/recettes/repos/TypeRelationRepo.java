package com.gestion.recettes.repos;

import com.gestion.recettes.entities.TypeRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRelationRepo extends JpaRepository<TypeRelation, Long> {
}
