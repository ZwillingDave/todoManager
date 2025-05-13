package com.example.todoManager.config.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.todoManager.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Autowired
    public JWTAuthFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.split(" ")[1].trim();
                tokenService.validateToken(token);
                String username = tokenService.getUserNameFromToken(token);
                UserDetails userDetails = new User(username, "", Collections.singletonList(new SimpleGrantedAuthority("USER")));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JWTVerificationException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
