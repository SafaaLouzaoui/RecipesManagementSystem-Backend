package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentairePersonneContactDto {
    private Long id;
    private String username;
    private String email;
    private String message;
    private Timestamp createdAt;
    private String objet;
}
