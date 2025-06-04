package com.musicbooking.config;

import com.musicbooking.model.ERole;
import com.musicbooking.model.Role;
import com.musicbooking.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(1)  // Ensures this runs before other initializers
public class DataLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    
    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        try {
            initializeRoles();
        } catch (Exception e) {
            logger.error("Failed to initialize roles", e);
        }
    }

    private void initializeRoles() {
        for (ERole roleName : ERole.values()) {
            createRoleIfNotExists(roleName);
        }
    }

    private void createRoleIfNotExists(ERole roleName) {
        if (!roleRepository.existsByName(roleName)) {
            try {
                Role role = new Role(roleName);
                roleRepository.save(role);
                logger.info("Created role: {}", roleName.name());
            } catch (DataIntegrityViolationException e) {
                logger.warn("Role {} already exists", roleName.name());
            }
        }
    }
}