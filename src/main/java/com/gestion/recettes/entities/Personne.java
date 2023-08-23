package com.gestion.recettes.entities;

import com.gestion.recettes.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Personne implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column
    private String nomComplet;

    @Basic
    @Column(unique=true)
    private String user_name;


    @Basic
    @Column(columnDefinition = "Date")
    private LocalDate dateCreation;

    @Basic
    @Column(columnDefinition = "Text")
    private String image;

    @Basic
    @Column(unique=true)
    private String adresseMail;

    @Basic
    @Column
    private String addressMailContact;

    @Basic
    @Column
    private String motDePasse;

    @Basic
    @Column
    private String statut;

    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "personne")
    private List<Token> tokens;

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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return motDePasse;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return adresseMail;
	}

}
