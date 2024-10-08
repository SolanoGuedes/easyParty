package com.solanoguedes.OER.utils;

import com.solanoguedes.OER.security.UsuarioSpringSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    // Método para obter o ID do usuário autenticado
    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UsuarioSpringSecurity) {
                return ((UsuarioSpringSecurity) principal).getId();
            }
        }
        return null;
    }
}