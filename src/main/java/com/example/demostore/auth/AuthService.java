package com.example.demostore.auth;

import com.example.demostore.dtos.auth.JwtResponse;
import com.example.demostore.dtos.auth.LoginRequest;
import com.example.demostore.dtos.auth.RegisterRequest;
import com.example.demostore.enums.UserRole;
import com.example.demostore.exceptions.ResourceAlreadyExistsException;
import com.example.demostore.exceptions.ResourceNotFoundException;
import com.example.demostore.model.Role;
import com.example.demostore.model.User;
import com.example.demostore.repo.RoleRepository;
import com.example.demostore.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        log.info("Authenticated successfully {}", loginRequest.getUsername());

        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        log.info("Registering user {}", registerRequest.getUsername());

        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            log.info("User with {} username already exists", registerRequest.getUsername());
            throw new ResourceAlreadyExistsException("User with username" + registerRequest.getUsername() + " already exists");
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            log.info("User with email {} already exists", registerRequest.getEmail());
            throw new ResourceAlreadyExistsException("User with email" + registerRequest.getEmail() + " already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .active(true)
                .build();

        Set<String> requestRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(requestRoles == null || requestRoles.isEmpty()) {
            Role customer = roleRepository.findByName(UserRole.CUSTOMER).orElseThrow(() -> new ResourceNotFoundException("Role", "name", UserRole.CUSTOMER)); //default role
            roles.add(customer);
        } else {
            requestRoles.forEach(roleName -> {
                switch (roleName) {
                    case "admin" -> {
                        Role admin = roleRepository.findByName(UserRole.ADMIN).orElseThrow(() -> new ResourceNotFoundException("Role", "name", UserRole.ADMIN));
                        roles.add(admin);
                    }
                    case "manager" -> {
                        Role manager = roleRepository.findByName(UserRole.MANAGER).orElseThrow(() -> new ResourceNotFoundException("Role", "name", UserRole.MANAGER));
                        roles.add(manager);
                    }
                    case "employee" -> {
                        Role employee = roleRepository.findByName(UserRole.EMPLOYEE).orElseThrow(() -> new ResourceNotFoundException("Role", "name", UserRole.EMPLOYEE));
                        roles.add(employee);
                    }
                    default -> {
                        Role customer = roleRepository.findByName(UserRole.CUSTOMER).orElseThrow(() -> new ResourceNotFoundException("Role", "name", UserRole.CUSTOMER));
                        roles.add(customer);
                    }
                }
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        log.info("Saved user {}", savedUser.getId());

        return savedUser;
    }
}
