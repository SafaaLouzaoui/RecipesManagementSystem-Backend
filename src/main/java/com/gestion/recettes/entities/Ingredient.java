package com.gestion.recettes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;
    @Column(name = "url")
    private String media;


    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<Recette> recettes = new ArrayList<>();

    @ManyToOne
    private Ingredient ingredientReference;


    @OneToMany(mappedBy = "ingredient", fetch = FetchType.EAGER)
    private List<Quantite> quantites = new ArrayList<>();

}
