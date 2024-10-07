package com.solanoguedes.OER.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solanoguedes.OER.model.enums.ProfileEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username não pode ser vazio.")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Email(message = "Email deve ser válido.")
    @NotBlank(message = "Email não pode ser vazio.")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Senha não pode ser vazia.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    @Column(nullable = false)
    private String senha;

    @Size(max = 255, message = "A bio pode ter no máximo 255 caracteres.")
    @Column(nullable = true, length = 255)
    private String bio;

    @NotNull(message = "O perfil público ou privado deve ser informado.")
    @Column(nullable = false)
    private boolean publico;

    @NotNull(message = "A reputação não pode ser nula.")
    @Column(nullable = false)
    private Integer reputacao = 0;

    @Column(nullable = true)
    private String localizacao;

    @Column(nullable = true, length = 255)
    private String instagramConectado;

    @NotNull(message = "O status ativo da conta deve ser informado.")
    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = true)
    private String urlFotoPerfil;

    @Column(name = "profile", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_profile")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }

    // Construtor que aceita um ID
    public Usuario(Long id) {
        this.id = id;
    }
}