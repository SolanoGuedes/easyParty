package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.dto.LoginRequest;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Optional;
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Rota para cadastro de usuário
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.ok(novoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    // Rota para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = usuarioService.login(loginRequest.getUsername(), loginRequest.getSenha());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer login: " + e.getMessage());
        }
    }

    // Rota para atualizar um usuário
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // Rota para deletar um usuário
    @DeleteMapping("/deletar/{username}")
    public ResponseEntity<?> deletarUsuario(@PathVariable String username) {
        try {
            usuarioService.deletarUsuario(username);
            return ResponseEntity.ok("Usuário deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    // Rota para consultar usuário pelo username
    @GetMapping("/consultar/{username}")
    public ResponseEntity<?> consultarUsuario(@PathVariable String username) {
        try {
            Usuario usuario = usuarioService.consultarPorUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao consultar usuário: " + e.getMessage());
        }
    }
}