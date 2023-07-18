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
@Table(name = "categories")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cat")
    private Long idCat;

    @Column(name = "nom_cat", nullable = false)
    private String nomCat;

    @Column(name = "description_cat")
    private String descriptionCat;

    @Column(name = "url")
    private String media;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private List<Recette> recettes = new ArrayList<>();
}
