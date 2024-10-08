package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.dto.LoginRequest;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.repositories.SeguidorRepository;
import com.solanoguedes.OER.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.solanoguedes.OER.utils.UserUtil.getAuthenticatedUserId;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private SeguidorRepository seguidorRepository;

    @Autowired
    private UsuarioService usuarioService;

    // Rota para cadastro de usuário
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Usuario usuario)  {
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
    @PatchMapping("/atualizar")
    public ResponseEntity<?> atualizarUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            Long id = getAuthenticatedUserId();
            Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // Rota para deletar um usuário
    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletarUsuario() {
        try {
            Long id = getAuthenticatedUserId();
            usuarioService.deletarUsuario(id);
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
