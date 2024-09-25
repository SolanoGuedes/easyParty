package com.solanoguedes.OER.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
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
    private boolean isPublico;

    @NotNull(message = "A reputação não pode ser nula.")
    @Column(nullable = false)
    private int reputacao = 0;

    @Column(nullable = true)
    private String localizacao;

    @Column(nullable = true, length = 255)
    private String instagramConectado;

    @NotNull(message = "O status ativo da conta deve ser informado.")
    @Column(nullable = false)
    private boolean ativo = true;

    // Construtor padrão
    public Usuario() {}

    // Getters e Setters

    public @NotBlank(message = "Username não pode ser vazio.") @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username não pode ser vazio.") @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.") String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Nome não pode ser vazio.") @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.") String getNome() {
        return nome;
    }

    public void setNome(@NotBlank(message = "Nome não pode ser vazio.") @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.") String nome) {
        this.nome = nome;
    }

    public @Email(message = "Email deve ser válido.") @NotBlank(message = "Email não pode ser vazio.") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email deve ser válido.") @NotBlank(message = "Email não pode ser vazio.") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Senha não pode ser vazia.") @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.") String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank(message = "Senha não pode ser vazia.") @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.") String senha) {
        this.senha = senha;
    }

    public @Size(max = 255, message = "A bio pode ter no máximo 255 caracteres.") String getBio() {
        return bio;
    }

    public void setBio(@Size(max = 255, message = "A bio pode ter no máximo 255 caracteres.") String bio) {
        this.bio = bio;
    }

    @NotNull(message = "O perfil público ou privado deve ser informado.")
    public boolean isPublico() {
        return isPublico;
    }

    public void setPublico(@NotNull(message = "O perfil público ou privado deve ser informado.") boolean publico) {
        isPublico = publico;
    }

    @NotNull(message = "A reputação não pode ser nula.")
    public int getReputacao() {
        return reputacao;
    }

    public void setReputacao(@NotNull(message = "A reputação não pode ser nula.") int reputacao) {
        this.reputacao = reputacao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getInstagramConectado() {
        return instagramConectado;
    }

    public void setInstagramConectado(String instagramConectado) {
        this.instagramConectado = instagramConectado;
    }

    @NotNull(message = "O status ativo da conta deve ser informado.")
    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(@NotNull(message = "O status ativo da conta deve ser informado.") boolean ativo) {
        this.ativo = ativo;
    }

}
