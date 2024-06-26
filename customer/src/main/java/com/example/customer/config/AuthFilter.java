package com.example.customer.config;

import com.example.customer.contants.ErrorMessage;
import com.example.customer.config.exception.CustomException;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomerRepository repository;

    @Autowired
    public AuthFilter(JwtService jwtService, CustomerRepository repository) {
        this.jwtService = jwtService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("request came");
        final var header = Optional.ofNullable(request.getHeader("Authorization"));
        header.ifPresent(this::authenticate);

        filterChain.doFilter(request, response);
    }

    private void authenticate(String header) {

        if (!header.startsWith("Bearer "))
            throw new CustomException(ErrorMessage.INVALID_TOKEN);

        final var token = header.substring(7);

        final var customerId = jwtService.extractId(token);

        if (!repository.existsByIdAndActiveTrue(customerId))
            throw new CustomException(ErrorMessage.WRONG_CREDENTIALS);

        final var role = jwtService.extractSubject(token);

        var authentication = new UsernamePasswordAuthenticationToken(customerId, null, getAuthorities(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        GrantedAuthority authority = new SimpleGrantedAuthority(String.format("ROLE_%s", role));
        return Collections.singleton(authority);
    }
}
