package com.jendo.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String header = request.getHeader("Authorization");
            String token = null;
            String email = null;

            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7).trim();
                
                if (!token.isEmpty() && jwtUtil.validateJwtToken(token)) {
                    if (jwtUtil.isAccessToken(token)) {
                        email = jwtUtil.getEmailFromToken(token);
                        log.debug("Valid access token found for email: {}", email);
                    } else {
                        log.warn("Non-access token used in Authorization header");
                    }
                } else {
                    log.debug("Invalid or expired JWT token");
                }
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    
                    log.debug("Successfully authenticated user: {}", email);
                } catch (UsernameNotFoundException e) {
                    log.warn("User not found for token email: {}", email);
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT authentication: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
}