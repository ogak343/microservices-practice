package com.example.customer.config;

import com.example.customer.contants.ErrorMessage;
import com.example.customer.exception.CustomException;
import com.example.customer.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    public AuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final var header = Optional.ofNullable(request.getHeader("Authorization"));
        header.ifPresent(this::authenticate);

        filterChain.doFilter(request, response);
    }

    private void authenticate(String header) {

        if (!header.startsWith("Bearer "))
            throw new CustomException(ErrorMessage.INVALID_TOKEN);

        var token = header.substring(7);

        var customerId = jwtService.extractCustomerId(token);

        var authentication = new UsernamePasswordAuthenticationToken(customerId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
