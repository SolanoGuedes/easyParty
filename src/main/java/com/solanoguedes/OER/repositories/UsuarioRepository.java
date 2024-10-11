package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Transactional(readOnly = true)
    Optional<Usuario> findByUsername(String username);
}
