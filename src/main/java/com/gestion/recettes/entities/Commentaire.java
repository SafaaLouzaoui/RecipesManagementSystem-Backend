package com.gestion.recettes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String message;
    @Column(precision = 5, scale = 4)
    private BigDecimal note;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;


    @ManyToOne    //utilisateur
    private Personne createurRecette;

    @ManyToOne(fetch = FetchType.EAGER)
    private Recette recette;

    @ManyToOne
    private Personne proprietaire;

    @ManyToOne   //modérateur
    private Personne ApprobateurRecette;
}
