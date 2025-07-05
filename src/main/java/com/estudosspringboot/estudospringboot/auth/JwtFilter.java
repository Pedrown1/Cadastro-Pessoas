package com.estudosspringboot.estudospringboot.auth;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.equals("/api/auth") ||
                path.equals("/api/pessoa/cadastro") ||
                path.equals("/api/pessoa/confirmar-email") ||
                path.equals("/api/pessoa/reenviar-codigo") ||
                path.equals("/cadastro") ||
                path.equals("/login") ||
                path.equals("/confirmacao") ||
                path.equals("/estabelecimento") || path.equals("/estabelecimento/local.html") ||
                path.startsWith("/css") ||
                path.startsWith("/js") ||
                path.startsWith("/images")) {

            filterChain.doFilter(request, response);
            return;
        }



        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");

            try {
                String username = JwtUtil.validateToken(token);

                if (username != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.emptyList());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }

            } catch (JwtException | IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\n" +
                        "  \"status\": 4,\n" +
                        "  \"descrição\": \"Token ausente ou inválido.\",\n" +
                        "  \"Dados\": []\n" +
                        "}");
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\n" +
                "  \"status\": 4,\n" +
                "  \"descrição\": \"Token ausente ou inválido.\",\n" +
                "  \"Dados\": []\n" +
                "}");
    }
}
