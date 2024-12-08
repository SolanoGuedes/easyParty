package com.solanoguedes.OER.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @NotBlank(message = "O username não pode ser vazio.")
    private String username;

    @NotBlank(message = "A senha não pode ser vazia.")
    private String senha;

}