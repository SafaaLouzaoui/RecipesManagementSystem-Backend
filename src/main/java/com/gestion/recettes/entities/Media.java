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
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(columnDefinition = "Text")
    private String url;


    @ManyToOne
    private Personne personne;

    @ManyToOne
    private MediaType mediaType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recette_id")
    private Recette recette;
}
