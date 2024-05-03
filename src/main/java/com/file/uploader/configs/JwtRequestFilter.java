package com.file.uploader.configs;

import com.file.uploader.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.SimpleErrors;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(!request.toString().startsWith("/api/")) {
            filterChain.doFilter(request, response);
        }

        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Token from authentication Bearer
            try {
                username = jwtTokenUtils.getUsernameFromToken(jwt);
            } catch (ExpiredJwtException e) { // Если токен истёк
                throw new JwtException("Срок действия JWT токена истек");
            } catch (SignatureException e) { // Если подпись нерпавильная
                throw new JwtException("Неправильная подпись для JWT токена");
            }
        }
        if(username != null && SecurityContextHolder.getContext() == null) {
            List<String> roles = jwtTokenUtils.getRoles(jwt);
            List<GrantedAuthority> authorities =
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null, // Credentionals (Полномочия)
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
