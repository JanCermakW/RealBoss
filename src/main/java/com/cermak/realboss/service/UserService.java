package com.cermak.realboss.service;

import com.cermak.realboss.model.Role;
import com.cermak.realboss.model.User;
import com.cermak.realboss.web.dto.UserRegistrationDto;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
    User saveUserWithRelation(User user) throws MessagingException, UnsupportedEncodingException;
    List<User> getAllUsers();

    User getUserById(Long id);
    User getUserByEmail(String email);
    User updateUser(User user);

    boolean existsById(Long id);

    void deleteUserById(Long id);

    void updateUserRoles(Long id, Collection<Role> newRoles);
    User saveUserStartup(User user);

    String encodePasswd(String passwd);

    void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);

    void sendForgotPasswd(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verifyForgotPasswd(String verificationCode);

    void sendEmail(User user, String content, String subject) throws MessagingException, UnsupportedEncodingException;
    boolean validatePassword(String password);
}
