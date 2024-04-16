package com.cermak.realboss.web;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import com.cermak.realboss.model.User;
import com.cermak.realboss.service.UserRelationService;
import com.cermak.realboss.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User realman = userService.getUserByEmail(currentPrincipalName);

        List<User> connectedUsers = userRelationService.getUsersByRealman(realman);

        model.addAttribute("connectedUsers", connectedUsers);
        model.addAttribute("newUser", new User());

        return "realman_users";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User newUser, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User realman = userService.getUserByEmail(currentPrincipalName);

        userService.saveUserWithRelation(newUser);

        User user = userService.getUserByEmail(newUser.getEmail());

        userService.sendVerificationEmail(user, getSiteURL(request));

        userRelationService.createRelation(realman, newUser);

        return "redirect:/realman/users";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUserRelation(@PathVariable Long id) {
        userRelationService.deleteRelationsByUserId(id);
        return "redirect:/realman/users";
    }

    @PostMapping("/createRelation")
    public String createRelation(@ModelAttribute User newUser, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User realman = userService.getUserByEmail(currentPrincipalName);

        User user = userService.getUserByEmail(newUser.getEmail());

        if (realman != null && user != null) {
            userRelationService.createRelation(realman, user);
        }

        return "redirect:/realman/users";
    }
}
