package com.cermak.realboss.web;

import com.cermak.realboss.model.Property;
import com.cermak.realboss.model.User;
import com.cermak.realboss.service.FileStorageService;
import com.cermak.realboss.service.PropertyService;
import com.cermak.realboss.service.UserService;
import org.aspectj.weaver.patterns.PerObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/realman")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/properties")
    public String showUserProperties(Model model, Principal principal) {
        // Get currently logged-in user
        User currentUser = userService.getUserByEmail(principal.getName());

        // Get properties associated with the current user
        List<Property> userProperties = propertyService.getPropertiesByRealman(currentUser);

        model.addAttribute("userProperties", userProperties);
        model.addAttribute("newProperty", new Property());
        model.addAttribute("newUser", new User());
        return "properties";
    }

    @PostMapping("/properties/create")
    public String createProperty(
            @ModelAttribute("property") Property property,
            BindingResult result,
            Principal principal,
            @RequestParam("file") MultipartFile file
    ) {
        if (result.hasErrors()) {
            return "properties";
        }

        // Get currently logged-in user
        User currentUser = userService.getUserByEmail(principal.getName());

        property.setRealman(currentUser);

        // Save the file and update the user's profile picture path
        String profilePicturePath = fileStorageService.storeFile(file);
        property.setMainPicturePath("img/" + profilePicturePath);

        // Save the property
        propertyService.saveProperty(property);

        return "redirect:/realman/properties";
    }

    @PostMapping("/properties/addUserProperty/{id}")
    public String createRelationUserProperty(@ModelAttribute User newUser, @PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        User user = userService.getUserByEmail(newUser.getEmail());
        property.setCustomer(user);
        propertyService.saveProperty(property);

        return "redirect:/realman/properties";
    }

    @GetMapping("/properties/delete/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deletPropertyById(id);
        return "redirect:/realman/properties";
    }
}
