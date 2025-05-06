package com.example.demostore.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(name="username", unique = true, nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    public record UserSummary(Long id, String username, String email, String fullName, Set<String> roles) {
        public static UserSummary fromUser(User user) {
            Set<String> roleNames = new HashSet<>();
            user.getRoles().forEach(role -> roleNames.add(String.valueOf(role.getName())));

            return new UserSummary(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName() + " " + user.getLastName(), roleNames);
        }
    }
}
