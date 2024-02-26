package com.cermak.realboss.web;

import com.cermak.realboss.model.Role;
import com.cermak.realboss.model.User;
import com.cermak.realboss.service.RoleService;
import com.cermak.realboss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private RoleService roleService;

    private UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));

        // Get all roles to populate in the form
        List<Role> allRoles = roleService.getAllRoles();
        model.addAttribute("allRoles", allRoles);
        return "edit_user";
    }

    @PostMapping("/admin/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, @RequestParam("roles") List<Long> roleIds , Model model) {
        //get student from db
        User existingUser = userService.getUserById(id);
        existingUser.setId(user.getId());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());

        // Get roles based on provided role IDs
        List<Role> newRoles = roleService.getRolesById(roleIds);

        // Update user roles
        existingUser.setRoles(newRoles);

        //save updated user objc
        userService.updateUser(existingUser);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }
}
