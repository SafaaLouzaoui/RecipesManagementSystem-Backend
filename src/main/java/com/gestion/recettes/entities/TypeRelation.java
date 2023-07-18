package com.gestion.recettes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TypeRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String description;


    @OneToMany(mappedBy = "typeRelation",fetch = FetchType.EAGER)
    private List<Recette> recettesLiees = new ArrayList<>();

    @OneToMany(mappedBy = "typeRel",fetch = FetchType.EAGER)
    private List<Recette> recettesLieesA = new ArrayList<>();

}
