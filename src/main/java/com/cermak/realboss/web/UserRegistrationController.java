package com.cermak.realboss.web;

import com.cermak.realboss.model.MailStructure;
import com.cermak.realboss.service.MailService;
import com.cermak.realboss.service.UserService;
import com.cermak.realboss.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @Autowired
    private MailService mailService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto){
        userService.save(registrationDto);

        String userEmail = registrationDto.getEmail();
        MailStructure mailStructure = new MailStructure();
        mailStructure.setSubject("RealBoss - Úspěšná registrace");
        mailStructure.setMessage("Děkujeme za registraci v naší aplikaci.");
        mailService.sendMail(userEmail, mailStructure);

        return "redirect:/registration?success";
    }
}
