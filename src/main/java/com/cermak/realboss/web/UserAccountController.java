package com.cermak.realboss.web;

import com.cermak.realboss.model.Role;
import com.cermak.realboss.model.User;
import com.cermak.realboss.service.FileStorageService;
import com.cermak.realboss.service.RoleService;
import com.cermak.realboss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class UserAccountController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/user")
    public String showUserDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userService.getUserByEmail(currentPrincipalName);

        model.addAttribute("user", currentUser);
        return "user";
    }

    @PostMapping("/user")
    public String updateUser(@ModelAttribute("user") User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userService.getUserByEmail(currentPrincipalName);

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhone(user.getPhone());
        currentUser.setCity(user.getCity());
        currentUser.setStreet(user.getStreet());
        currentUser.setPostNum(user.getPostNum());

        userService.updateUser(currentUser);
        return "redirect:/user?success";
    }

    @PostMapping("/user/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName());

        String profilePicturePath = fileStorageService.storeFile(file);
        currentUser.setProfilePicturePath("/img/" + profilePicturePath);

        userService.updateUser(currentUser);

        return "redirect:/user";
    }

    @PostMapping("/user/passwd")
    public String changePasswd(@ModelAttribute("user") User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User currentUser = userService.getUserByEmail(currentPrincipalName);

        if (!userService.validatePassword(user.getPassword())) {
            return "redirect:/user?errorPasswd";
        }

        currentUser.setPassword(userService.encodePasswd(user.getPassword()));



        userService.updateUser(currentUser);
        return "redirect:/user?success";
    }
}
