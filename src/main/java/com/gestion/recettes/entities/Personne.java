package com.gestion.recettes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column
    private String nomComplet;

    @Basic
    @Column
    private String username;


    @Basic
    @Column(columnDefinition = "Date")
    private LocalDate dateCreation;

    @Basic
    @Column
    private String image;

    @Basic
    @Column(unique=true)
    private String adresseMail;

    @Basic
    @Column
    private String motDePasse;

    @Basic
    @Column
    private String statut;

    @ManyToOne
    private Profile profile;

    @OneToMany(mappedBy = "proprietaire", fetch = FetchType.EAGER)
    private List<Commentaire> mesCommentaires;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Personne> abonnees;

    @OneToMany(mappedBy = "utilisateurCreateur", fetch = FetchType.EAGER)
    private List<Recette> mesRecettes;

    @OneToMany(mappedBy = "utilisateurApprobateur", fetch = FetchType.EAGER)
    private List<Recette> recettesApprouvees;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Recette> recettesSignalees;


    @OneToMany(mappedBy = "createurRecette", fetch = FetchType.EAGER)
    private List<Commentaire> commentaires;

    @OneToMany(mappedBy = "ApprobateurRecette", fetch = FetchType.EAGER)
    private List<Commentaire> remarques;

}
