package com.example.demostore.repo;

import com.example.demostore.enums.UserRole;
import com.example.demostore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);
    boolean existsByName(UserRole name);

}
