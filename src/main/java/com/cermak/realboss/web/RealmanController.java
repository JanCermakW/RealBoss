package com.cermak.realboss.web;

import org.springframework.ui.Model;
import com.cermak.realboss.model.User;
import com.cermak.realboss.service.UserRelationService;
import com.cermak.realboss.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/realman")
public class RealmanController {
    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listConnectedUsers(Model model) {
        // Get currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Assuming you have a method in your UserService to find a user by email
        User realman = userService.getUserByEmail(currentPrincipalName);

        // Get the list of connected ROLE_USER users
        List<User> connectedUsers = userRelationService.getUsersByRealman(realman);

        model.addAttribute("connectedUsers", connectedUsers);
        model.addAttribute("newUser", new User());

        return "realman_users";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User newUser, Model model) {
        // Get currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User realman = userService.getUserByEmail(currentPrincipalName);

        // Save the new user to the database
        userService.saveUserWithRelation(newUser);

        // Create a user relation between the logged-in realman and the newly added user
        userRelationService.createRelation(realman, newUser);

        return "redirect:/realman/users";
    }
}
