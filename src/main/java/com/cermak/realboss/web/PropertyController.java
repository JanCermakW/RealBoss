package com.cermak.realboss.web;

import com.cermak.realboss.model.Property;
import com.cermak.realboss.model.Role;
import com.cermak.realboss.model.User;
import com.cermak.realboss.model.UserRelation;
import com.cermak.realboss.service.FileStorageService;
import com.cermak.realboss.service.PropertyService;
import com.cermak.realboss.service.UserRelationService;
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
    private UserRelationService userRelationService;

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
        model.addAttribute("newUser", new User());
        return "properties";
    }

    @GetMapping("/properties/create")
    public String createProperty(Model model) {
        model.addAttribute("newProperty", new Property());
        return "create_property";
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

        User currentUser = userService.getUserByEmail(principal.getName());

        property.setRealman(currentUser);

        if (file.isEmpty()) {
            return "redirect:/realman/properties/create?ImgEmpty";
        }

        String profilePicturePath = fileStorageService.storeFile(file);
        property.setMainPicturePath("/img/" + profilePicturePath);

        propertyService.saveProperty(property);

        return "redirect:/realman/properties";
    }

    @PostMapping("/properties/addUserProperty/{id}")
    public String createRelationUserProperty(@ModelAttribute User newUser, @PathVariable Long id, Principal principal) {
        Property property = propertyService.getPropertyById(id);
        User user = userService.getUserByEmail(newUser.getEmail());

        User currentUser = userService.getUserByEmail(principal.getName());

        List<User> AllCustomers = userRelationService.getUsersByRealman(currentUser);

        if (user == null) {
            property.setCustomer(null);
            propertyService.saveProperty(property);
            return "redirect:/realman/properties";
        }

        for (User userLoop : AllCustomers) {
            if (userLoop.getId().equals(user.getId())) {
                property.setCustomer(user);
                propertyService.saveProperty(property);

                return "redirect:/realman/properties";
            }
        }
        return "redirect:/realman/properties?userNotCustomer";
    }

    @GetMapping("/properties/delete/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deletPropertyById(id);
        return "redirect:/realman/properties";
    }

    @GetMapping("/properties/edit/{id}")
    public String editPropertyForm(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.getPropertyById(id));
        return "edit_property";
    }

    @PostMapping("/properties/edit/{id}")
    public String updateProperty(@PathVariable Long id, @ModelAttribute("property") Property property, @RequestParam("file") MultipartFile file,Model model) {

        Property existingProperty = propertyService.getPropertyById(id);
        existingProperty.setName(property.getName());
        existingProperty.setCity(property.getCity());
        existingProperty.setDescription(property.getDescription());
        existingProperty.setPostNum(property.getPostNum());
        existingProperty.setPrice(property.getPrice());
        existingProperty.setStreet(property.getStreet());

        if (!property.getPriceNote().isEmpty()) {
            existingProperty.setPriceNote(property.getPriceNote());
        }

        if (!property.getBuilding().isEmpty()) {
            existingProperty.setBuilding(property.getBuilding());
        }

        if (!property.getObjectState().isEmpty()) {
            existingProperty.setObjectState(property.getObjectState());
        }

        if (!property.getObjectPlacement().isEmpty()) {
            existingProperty.setObjectPlacement(property.getObjectPlacement());
        }

        if (property.getUsableArea() != null) {
            existingProperty.setUsableArea(property.getUsableArea());
        }

        if (property.getFloorArea() != null) {
            existingProperty.setFloorArea(property.getFloorArea());
        }

        if (!property.getFloor().isEmpty()) {
            existingProperty.setFloor(property.getFloor());
        }

        if (property.getBasement() != null) {
            existingProperty.setBasement(property.getBasement());
        }

        if (property.getApprovalYear() != null) {
            existingProperty.setApprovalYear(property.getApprovalYear());
        }

        if (property.getReconstructionYear() != null) {
            existingProperty.setReconstructionYear(property.getReconstructionYear());
        }

        if (!property.getWater().isEmpty()) {
            existingProperty.setWater(property.getWater());
        }

        if (!property.getElectricity().isEmpty()) {
            existingProperty.setElectricity(property.getElectricity());
        }

        if (!property.getCommunicationRoad().isEmpty()) {
            existingProperty.setCommunicationRoad(property.getCommunicationRoad());
        }

        if (!file.isEmpty()) {
            String profilePicturePath = fileStorageService.storeFile(file);
            existingProperty.setMainPicturePath("/img/" + profilePicturePath);
        }


        propertyService.saveProperty(existingProperty);
        return "redirect:/realman/properties";
    }

    @GetMapping("/properties/view/{id}")
    public String viewProperty(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.getPropertyById(id));
        return "view_property";
    }
}
