package dev.openweb.api.auth;

import dev.openweb.api.user.UserRepository;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {
    private final UserRepository users;
    public MeController(UserRepository users) { this.users = users; }

    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {
        var email = principal.getAttribute("email");
        var user = email == null ? null : users.findByEmailIgnoreCase(email).orElse(null);
        if (user == null) throw new IllegalStateException("Authenticated user is not registered.");
        return Map.of("id", user.getId(), "email", user.getEmail(), "displayName", user.getDisplayName());
    }
}