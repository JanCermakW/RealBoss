package com.cermak.realboss.web;
import com.cermak.realboss.model.Role;
import com.cermak.realboss.repository.RoleRepository;
import com.cermak.realboss.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.cermak.realboss.model.User;
import com.cermak.realboss.service.UserService;

import java.util.Arrays;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_REALMAN");

        User user = userService.getUserByEmail("admin@example.com");
        if (user == null) {

            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@example.com");
            adminUser.setEnabled(true);
            adminUser.setPassword(passwordEncoder.encode("admin"));

            adminUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN").get()));

            userService.saveUserStartup(adminUser);
        }
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleService.existsByName(roleName)) {
            Role role = new Role(roleName);
            roleService.saveRole(role);
        }
    }
}

