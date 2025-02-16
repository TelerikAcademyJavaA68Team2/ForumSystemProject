package com.example.forumproject.helpers;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MvcBlockedUserFilter extends OncePerRequestFilter {

    private final AccountStatusUserDetailsChecker accountStatusChecker = new AccountStatusUserDetailsChecker();

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        if (isPublicRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            try {
                accountStatusChecker.check(userDetails);
            } catch (DisabledException e) {
                request.getSession().invalidate();
                response.sendRedirect("/mvc/blocked");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicRequest(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return requestUri.startsWith("/mvc/auth/login") || requestUri.startsWith("/mvc/auth/register") || requestUri.startsWith("/mvc/home");
    }
}