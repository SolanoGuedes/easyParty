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
            // Atualiza somente os campos que não são nulos
            if (usuarioAtualizado.getNome() != null) {
                usuario.setNome(usuarioAtualizado.getNome());
            }
            if (usuarioAtualizado.getEmail() != null) {
                usuario.setEmail(usuarioAtualizado.getEmail());
            }
            if (usuarioAtualizado.getSenha() != null) {
                usuario.setSenha(usuarioAtualizado.getSenha());
            }
            if (usuarioAtualizado.getBio() != null) {
                usuario.setBio(usuarioAtualizado.getBio());
            }
            if (usuarioAtualizado.isPublico() != usuario.isPublico()) {
                usuario.setPublico(usuarioAtualizado.isPublico());
            }
            if (usuarioAtualizado.getReputacao() != null) {
                usuario.setReputacao(usuarioAtualizado.getReputacao());
            }
            if (usuarioAtualizado.getLocalizacao() != null) {
                usuario.setLocalizacao(usuarioAtualizado.getLocalizacao());
            }
            if (usuarioAtualizado.getInstagramConectado() != null) {
                usuario.setInstagramConectado(usuarioAtualizado.getInstagramConectado());
            }
            if (usuarioAtualizado.getUrlFotoPerfil() != null) {
                usuario.setUrlFotoPerfil(usuarioAtualizado.getUrlFotoPerfil());
            }
            if (usuarioAtualizado.isAtivo() != usuario.isAtivo()) {
                usuario.setAtivo(usuarioAtualizado.isAtivo());
            }

            // Verifica se a senha foi alterada, caso sim, criptografa
            if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                usuario.setSenha(bCryptPasswordEncoder.encode(usuarioAtualizado.getSenha()));
            }

            return usuarioRepository.save(usuario);
        } else {
            throw new Exception("Usuário não encontrado.");
        }
    }


    // Método para deletar usuário pelo username
    public void deletarUsuario(Long id) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
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
