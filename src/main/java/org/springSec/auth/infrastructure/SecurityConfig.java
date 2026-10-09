package org.springSec.auth.infrastructure;

import java.util.List;

import org.springSec.auth.domain.UserRole;
import org.springSec.auth.infrastructure.persistence.entity.User;
import org.springSec.auth.infrastructure.persistence.repository.UserRepository;
import org.springSec.auth.infrastructure.security.RestUsernamePasswordAuthenticationFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean 
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            AuthenticationConfiguration authenticationConfiguration, ObjectMapper objectMapper) throws Exception {
        var restUsernamePasswordAuthenticationFilter = new RestUsernamePasswordAuthenticationFilter(objectMapper);
        restUsernamePasswordAuthenticationFilter
                .setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());

        http
            .csrf(AbstractHttpConfigurer::disable)
            .securityContext(context -> context.requireExplicitSave(false))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterAt(restUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                User fitnessUser = new User();
                fitnessUser.setUsername("fitness_vibe");
                fitnessUser.setPassword(passwordEncoder.encode("password"));
                fitnessUser.setRole(UserRole.ROLE_USERNAME);

                User techUser = new User();
                techUser.setUsername("tech_guru");
                techUser.setPassword(passwordEncoder.encode("password"));
                techUser.setRole(UserRole.ROLE_USERNAME);

                User brand = new User();
                brand.setUsername("logistics");
                brand.setPassword(passwordEncoder.encode("password"));
                brand.setRole(UserRole.ROLE_BRAND);

                repository.saveAll(List.of(fitnessUser, techUser, brand));
            }
    };
    }
}
