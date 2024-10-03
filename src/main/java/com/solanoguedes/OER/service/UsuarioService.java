package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.enums.ProfileEnum;
import com.solanoguedes.OER.repositories.UsuarioRepository;
import com.solanoguedes.OER.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    // Método de login
    public String login(String username, String senha) throws Exception {
        Usuario usuario = consultarPorUsername(username);
        if (usuario != null && bCryptPasswordEncoder.matches(senha, usuario.getSenha())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new Exception("Usuário ou senha inválidos.");
        }
    }

    // Método para cadastrar usuário (com criptografia de senha)
    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new Exception("O username já está em uso.");
        }
        usuario.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));  // Criptografando a senha
        return usuarioRepository.save(usuario);
    }

    // Método para atualizar informações do usuário
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) throws Exception {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setBio(usuarioAtualizado.getBio());
            usuario.setPublico(usuarioAtualizado.isPublico());
            usuario.setLocalizacao(usuarioAtualizado.getLocalizacao());
            // Verifica se a senha foi alterada, caso sim, criptografa
            if (!usuarioAtualizado.getSenha().isEmpty()) {
                usuario.setSenha(bCryptPasswordEncoder.encode(usuarioAtualizado.getSenha()));
            }
            return usuarioRepository.save(usuario);
        } else {
            throw new Exception("Usuário não encontrado.");
        }
    }

    // Método para deletar usuário pelo username
    public void deletarUsuario(String username) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
        } else {
            throw new Exception("Usuário não encontrado.");
        }
    }

    // Método para consultar usuário por username
    public Usuario consultarPorUsername(String username) throws Exception {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Usuário não encontrado."));
    }
}
