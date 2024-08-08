package com.beyond.teenkiri.user.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtAuthFilter extends GenericFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static final List<String> EXCLUDE_URLS = List.of(
            "/user/login",
            "/user/register",
            "/user/find-id",
            "/user/find-password",
            "/user/send-verification-code",
            "/user/verify-email"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();

        if (EXCLUDE_URLS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        try {
            if (bearerToken != null) {
                if (!bearerToken.startsWith("Bearer ")) {
                    throw new AuthenticationServiceException("Not in BEARER format");
                }
                String token = bearerToken.substring(7);

                // Check if the token contains exactly 2 period characters
                if (token.chars().filter(ch -> ch == '.').count() != 2) {
                    throw new AuthenticationServiceException("JWT strings must contain exactly 2 period characters.");
                }

                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error(e.getMessage());

            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("{\"error\": \"Unauthorized\"}");
        }
    }
}
