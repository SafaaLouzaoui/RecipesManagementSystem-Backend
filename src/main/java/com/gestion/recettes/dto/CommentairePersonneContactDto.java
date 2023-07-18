package com.gestion.recettes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentairePersonneContactDto {
    private Long id;
    private String username;
    private String email;
    private String message;
    private String objet;
}
