package dev.openweb.api.auth;

import dev.openweb.api.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, UserRepository users) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/error", "/oauth2/**", "/login/**", "/api/health").permitAll()
                .requestMatchers("/api/me").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth -> oauth
                .successHandler((request, response, authentication) -> {
                    var principal = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
                    var subject = principal.getAttribute("sub");
                    var email = principal.getAttribute("email");
                    var name = principal.getAttribute("name");
                    if (subject == null || email == null) {
                        response.sendError(400, "Google account did not provide required identity information.");
                        return;
                    }
                    var user = users.findByGoogleSubject(subject)
                        .orElseGet(() -> users.findByEmailIgnoreCase(email)
                            .map(existing -> {
                                existing.setGoogleSubject(subject);
                                if (name != null && !name.isBlank()) existing.setDisplayName(name);
                                return existing;
                            })
                            .orElseGet(() -> new dev.openweb.api.user.User(email, name == null || name.isBlank() ? email : name, subject)));
                    users.save(user);
                    response.sendRedirect("http://localhost:5173");
                })
            )
            .logout(logout -> logout.logoutSuccessUrl("http://localhost:5173"));
        return http.build();
    }
}