package com.gestion.recettes.response;

import lombok.Data;

@Data
public class LoginResponse {
     String message;
     Boolean status;
     Long id;
     public LoginResponse(Long id, String message, boolean status) {
          this.id = id;
          this.message = message;
          this.status = status;
     }

}