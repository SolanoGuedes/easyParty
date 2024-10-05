package com.solanoguedes.OER.security;

import java.io.IOException;
import java.util.ArrayList;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solanoguedes.OER.exceptions.GlobalExceptionHandler;
import com.solanoguedes.OER.model.Usuario;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Authentication attemptAuthentication(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) {
        try {
            Usuario userCredentials = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userCredentials.getUsername(), userCredentials.getSenha(), new ArrayList<>());

            Authentication authentication = this.authenticationManager.authenticate(authToken);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain chain, Authentication authResult)

            throws IOException, jakarta.servlet.ServletException {
        UsuarioSpringSecurity userSpringSecurity = (UsuarioSpringSecurity) authResult.getPrincipal();
        String username = userSpringSecurity.getUsername();
        String token = this.jwtUtil.generateToken(username);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
    }
}