package com.cermak.realboss.web;

import com.cermak.realboss.model.User;
import com.cermak.realboss.service.MailService;
import com.cermak.realboss.service.UserService;
import com.cermak.realboss.web.dto.UserRegistrationDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        if (userService.getUserByEmail(registrationDto.getEmail()) != null) {
            return "redirect:/registration?error";
        }

        if (!userService.validatePassword(registrationDto.getPassword())) {
            return "redirect:/registration?errorPasswd";
        }

        userService.save(registrationDto);

        User currentUser = userService.getUserByEmail(registrationDto.getEmail());

        userService.sendVerificationEmail(currentUser, getSiteURL(request));

        return "redirect:/registration?success";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_email_success";
        } else {
            return "verify_email_fail";
        }
    }
}
