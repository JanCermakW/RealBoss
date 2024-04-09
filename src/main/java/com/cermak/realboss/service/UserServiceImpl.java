package com.cermak.realboss.service;

import com.cermak.realboss.exception.UserNotFoundException;
import com.cermak.realboss.model.Property;
import com.cermak.realboss.model.Role;
import com.cermak.realboss.model.User;
import com.cermak.realboss.model.UserRelation;
import com.cermak.realboss.repository.PropertyRepository;
import com.cermak.realboss.repository.RoleRepository;
import com.cermak.realboss.repository.UserRelationRepository;
import com.cermak.realboss.repository.UserRepository;
import com.cermak.realboss.web.PasswordGenerator;
import com.cermak.realboss.web.dto.UserRegistrationDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                Arrays.asList(roleRepository.findByName("ROLE_USER").get())
        );

        String randomCode = PasswordGenerator.generateRandomPassword(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        return userRepository.save(user);
    }

    @Override
    public User saveUserWithRelation(User user) throws MessagingException, UnsupportedEncodingException {
        String password = PasswordGenerator.generateRandomPassword(12);

        String content = "Milý [[name]],<br>"
                + "Zde zasíláme nové heslo k vašemu účtu, ihned po přihlášení si ho změňte!!:<br>"
                + "<h3>[[password]]</h3>"
                + "Děkujeme,<br>"
                + "Realboss team.";
        String subject = "Nový účet - Realboss";

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        content = content.replace("[[password]]", password);

        sendEmail(user, content, subject);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));

        String randomCode = PasswordGenerator.generateRandomPassword(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Špatné jméno nebo heslo!");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("Účet není aktivní");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<Property> properties = propertyRepository.findByCustomer(user);
            List<Property> propertiesRealman = propertyRepository.findByRealman(user);
            if (properties.isEmpty() && propertiesRealman.isEmpty()) {
                userRepository.deleteById(id);
            } else if (!properties.isEmpty()){
                for (Property property : properties) {
                    property.setCustomer(null);
                    propertyRepository.save(property);
                }
                userRepository.deleteById(id);
            } else if (!propertiesRealman.isEmpty()) {
                for (Property property : propertiesRealman) {
                    propertyRepository.delete(property);
                }
                userRepository.deleteById(id);
            }
        }
    }

    @Override
    public void updateUserRoles(Long userId, Collection<Role> newRoles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setRoles(newRoles);
        userRepository.save(user);
    }

    @Override
    public User saveUserStartup(User user) {
        return userRepository.save(user);
    }

    @Override
    public String encodePasswd(String passwd) {
        return passwordEncoder.encode(passwd);
    }

    @Override
    public void sendEmail(User user, String content, String subject) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "honzacermak74@gmail.com";
        String senderName = "Realboss.co";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);

    }

    @Override
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String content = "Milý [[name]],<br>"
                + "Kliknutím na odkaz níže ověřte svou registraci:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">OVĚŘIT</a></h3>"
                + "Děkujeme,<br>"
                + "Realboss team.";
        String subject = "Ověřte svůj email - Realboss";

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyURL = siteURL + "/registration/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        sendEmail(user, content, subject);

    }

    @Override
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }
    }

    @Override
    public void sendForgotPasswd(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String randomCode = PasswordGenerator.generateRandomPassword(64) + user.getEmail();
        user.setVerificationCode(randomCode);
        userRepository.save(user);

        String content = "Milý [[name]],<br>"
                + "Kliknutím na odkaz níže si necháte poslat vygenerované heslo a ihned si ho po přihlášení změňte:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ZASLAT HESLO</a></h3>"
                + "Děkujeme,<br>"
                + "Realboss team.";
        String subject = "Zapomenuté heslo - Realboss";

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyURL = siteURL + "/forgotPasswd/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        sendEmail(user, content, subject);
    }

    @Override
    public boolean verifyForgotPasswd(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null) {
            return false;
        } else {
            user.setVerificationCode(null);
            userRepository.save(user);

            return true;
        }
    }

    @Override
    public boolean validatePassword(String password) {
        String regex = "^(?=.*[A-Z]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
