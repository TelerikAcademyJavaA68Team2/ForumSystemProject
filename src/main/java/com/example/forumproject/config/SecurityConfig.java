package com.example.forumproject.config;

import com.example.forumproject.helpers.CustomAuthenticationFailureHandler;
import com.example.forumproject.helpers.JwtAuthorizationFilter;
import com.example.forumproject.helpers.MvcBlockedUserFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_SWAGGER_URL = {"/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
            "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
            "/api/test/**", "/authenticate"};
    private static final String[] PUBLIC_URL_LIST = {"/api/home/**", "/error", "/", "/css/**", "/js/**", "/images/**"};
    private static final String[] PUBLIC_MVC_URL_LIST = {"/mvc/home/**", "/mvc/login","/mvc/register"};
    private static final String[] RESTRICTED_URL_LIST = {"/api/admin/**", "/api/users/**"};
    private static final String[] RESTRICTED_MVC_URL_LIST = {"/mvc/admin/**", "/mvc/users/**"};

    private final UserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final MvcBlockedUserFilter mvcBlockedUserFilter;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthorizationFilter authorizationFilter, MvcBlockedUserFilter mvcBlockedUserFilter, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthorizationFilter = authorizationFilter;
        this.mvcBlockedUserFilter = mvcBlockedUserFilter;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    @Order(1) // Higher priority
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/mvc/**")
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PUBLIC_MVC_URL_LIST).permitAll() // Public MVC endpoints
                        .requestMatchers(RESTRICTED_MVC_URL_LIST).hasAuthority("ADMIN") // Restricted MVC endpoints
                        .anyRequest().authenticated()) //All other requests authenticated
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Stateful for MVC
                .formLogin(form -> form
                        .loginPage("/mvc/login")
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/mvc/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/mvc/home"))
                .addFilterBefore(mvcBlockedUserFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/mvc/login"); // Redirect to login for unauthorized access
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/mvc/access-denied"); // Redirect to access-denied for forbidden access
                        }))
                .build();
    }

    @Bean
    @Order(2) // Lower priority
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**") // Apply this chain only to /api/**
                .authorizeHttpRequests(request -> request
                        .requestMatchers(WHITE_LIST_SWAGGER_URL).permitAll()
                        .requestMatchers(PUBLIC_URL_LIST).permitAll()
                        .requestMatchers(RESTRICTED_URL_LIST).hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> { // Handle 401 (Unauthorized)
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: You need to be logged in to access this page!");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> { // Handle 403 (Forbidden)
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: You don't have permission to access this page!");
                        }))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}