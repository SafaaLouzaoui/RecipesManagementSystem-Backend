package com.gestion.recettes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private String origine;
    private int dureePreparation;
    private int dureeCuisson;
    private int nbrPersonnes;
    @Column(name = "visibilitee", columnDefinition = "VARCHAR(255) DEFAULT 'public'")
    private String visibilitee;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int statut;

    @Column(columnDefinition = "DATE")
    private LocalDate dateSoumission;
    @Column(columnDefinition = "Date")
    private LocalDate datePublication;


    @ManyToOne
    @JoinColumn(name = "utilisateur_createur_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_utilisateur_createur_id"), nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Personne utilisateurCreateur;

    @ManyToOne
    @JoinColumn(name = "utilisateur_approbateur_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_utilisateur_approbateur_id"))
    private Personne utilisateurApprobateur;

    @ManyToMany(mappedBy = "recettesSignalees", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Personne> signalants = new ArrayList<>();

    @OneToMany(mappedBy = "recette", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Media> medias = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recette_besoins",
            joinColumns = @JoinColumn(name = "recettes_id"),
            inverseJoinColumns = @JoinColumn(name = "besoins_id"))
    private List<Besoin> besoins = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recette_mot_cles",
            joinColumns = @JoinColumn(name = "recettes_id"),
            inverseJoinColumns = @JoinColumn(name = "mot_cles_id"))
    private List<MotCle> motCles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recette_ingredients",
            joinColumns = @JoinColumn(name = "recettes_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredients_id"))
    private  List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recette", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Etape> etapes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recette_categories",
            joinColumns = @JoinColumn(name = "recettes_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id_cat"))
    private List<Categorie> categories = new ArrayList<>();


    @ManyToOne
    private TypeRelation typeRelation;

    @ManyToOne
    private TypeRelation typeRel;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "recette_recettes",
            joinColumns = @JoinColumn(name = "recette_id"),
            inverseJoinColumns = @JoinColumn(name = "recettes_id"))
    private List<Recette> recettes = new ArrayList<>();

    @OneToMany(mappedBy = "recette", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Quantite> quantites = new ArrayList<>();
    @OneToMany(mappedBy = "recette", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Commentaire> commentaires = new ArrayList<>();
}
