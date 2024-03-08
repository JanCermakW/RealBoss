package com.cermak.realboss.web;

import com.cermak.realboss.model.Property;
import com.cermak.realboss.model.User;
import com.cermak.realboss.service.PropertyService;
import com.cermak.realboss.service.UserRelationService;
import com.cermak.realboss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRelationService userRelationService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName());

        long propertyCount = propertyService.getPropertyCountForRealman(currentUser);
        model.addAttribute("propertyCount", propertyCount);

        long propertyCountCustomer = propertyService.getPropertyCountForCustomer(currentUser);
        model.addAttribute("propertyCountCustomer", propertyCountCustomer);

        long customersCount = userRelationService.countUsersByRealman(currentUser);
        model.addAttribute("customersCount", customersCount);
        List<Property> userProperties = propertyService.getPropertiesByRealman(currentUser);
        model.addAttribute("userProperties", userProperties);

        List<Property> customerProperties = propertyService.getPropertiesByCustomer(currentUser);
        model.addAttribute("customerProperties", customerProperties);

        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    /*
    @GetMapping("/error")
    public String error() {
        return "error";
    }*/
}
