package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.repositories.UsuarioRepository;
import com.solanoguedes.OER.security.UsuarioSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsuarioDetailsServiceimpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = this.usuarioRepository.findByUsername(username);
        Usuario usuario = usuarioOptional.orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado: " + username));
        return new UsuarioSpringSecurity(usuario.getId(), usuario.getUsername(), usuario.getSenha(), usuario.getProfiles());
    }
}
