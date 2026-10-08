package dev.openweb.api.user;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "display_name", nullable = false, length = 120)
    private String displayName;

    @Column(name = "google_subject", unique = true, length = 255)
    private String googleSubject;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected User() {}

    public User(String email, String displayName, String googleSubject) {
        this.email = email;
        this.displayName = displayName;
        this.googleSubject = googleSubject;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void touch() { updatedAt = Instant.now(); }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getGoogleSubject() { return googleSubject; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setGoogleSubject(String googleSubject) { this.googleSubject = googleSubject; }
}