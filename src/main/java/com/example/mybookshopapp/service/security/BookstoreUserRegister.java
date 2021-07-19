package com.example.mybookshopapp.service.security;

import com.example.mybookshopapp.entity.SmsCode;
import com.example.mybookshopapp.errs.WrongCredentialsException;
import com.example.mybookshopapp.dto.ChangeUserForm;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.util.security.JWTUtil;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;

    @Value("${appEmail.email}")
    private String email;

    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil, SmsService smsService, JavaMailSender javaMailSender) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
    }

    public BookstoreUser registerNewUser(RegistrationForm registrationForm) {

        BookstoreUser userByEmail = bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail());
        BookstoreUser userByPhone = bookstoreUserRepository.findBookstoreUserByPhone(registrationForm.getPhone());

        if (userByEmail == null && userByPhone == null) {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
            user.setHash(UUID.randomUUID().toString());
            bookstoreUserRepository.save(user);
            return user;
        } else {
            return userByPhone;
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                        payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        BookstoreUserDetails userDetails =
                (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public ContactConfirmationResponse jwtLoginByPhoneNumber(ContactConfirmationPayload payload) {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setPhone(payload.getContact());
        registrationForm.setPass(payload.getCode());
        registerNewUser(registrationForm);
        UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public Object getCurrentUser() {
        BookstoreUserDetails userDetails =
                (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getBookstoreUser();
    }

    public void updateUser(ChangeUserForm changeUserForm) throws WrongCredentialsException {
        BookstoreUser currentUser = (BookstoreUser) getCurrentUser();
        BookstoreUser updateUser = new BookstoreUser();
        if (verifyPassword(changeUserForm.getPassword(), changeUserForm.getPasswordReply())) {
            updateUser.setPassword(changeUserForm.getPassword());
        } else {
            throw new WrongCredentialsException("Incorrect password");
        }
        if (verifyUserName(changeUserForm.getName())) {
            updateUser.setName(changeUserForm.getName());
        } else {
            throw new WrongCredentialsException("Incorrect name");
        }
        if (verifyEmail(changeUserForm.getMail())) {
            updateUser.setEmail(changeUserForm.getMail());
        } else {
            throw new WrongCredentialsException("Incorrect email");
        }
        if (verifyPhone(changeUserForm.getPhone())) {
            updateUser.setPhone(changeUserForm.getPhone());
        } else {
            throw new WrongCredentialsException("Incorrect phone");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(currentUser.getEmail());
        SmsCode smsCode = new SmsCode(smsService.generateCode(), 300); //5 minutes
        smsService.saveNewCode(smsCode);
        message.setSubject("Changing credentials!");
        message.setText("Please, visit next link: http://localhost:8080/changeCredentials/"
                + bookstoreUserRepository.save(updateUser).getId() + "/"
                + currentUser.getId() + "/"
                + smsCode.getCode().replace(" ", "_"));
        javaMailSender.send(message);
    }

    public String approveCredentials(Integer updateUserId, Integer currentUserId, String code) throws WrongCredentialsException {
        BookstoreUser currentUser = bookstoreUserRepository.getOne(currentUserId);
        Optional<BookstoreUser> updateUser = bookstoreUserRepository.findById(updateUserId);

        if (Boolean.FALSE.equals(smsService.verifyCode(code.replace("_", " "))) || Boolean.FALSE.equals(updateUser.isPresent()) || !currentUser.getId().equals(currentUserId)) {
            throw new WrongCredentialsException(!updateUser.isPresent() ? "Changes already confirmed" : "Confirmation code expired");
        }
        currentUser.setName(updateUser.get().getName());
        currentUser.setEmail(updateUser.get().getEmail());
        currentUser.setPhone(updateUser.get().getPhone());
        currentUser.setPassword(passwordEncoder.encode(updateUser.get().getPassword()));

        String password = updateUser.get().getPassword();
        bookstoreUserRepository.delete(updateUser.get());
        bookstoreUserRepository.save(currentUser);

        String token = generateTokenForUpdateUser(currentUser, password);

        authenticateUpdatedUser(currentUser, token);

        return token;
    }

    private boolean verifyPhone(String phone) {
        return (phone != null && !phone.isEmpty());
    }

    private boolean verifyEmail(String email) {
        return  (email != null && !email.isEmpty() && email.contains("@"));
    }

    private boolean verifyUserName(String name) {
        return  (name != null && !name.isEmpty() && name.length() > 2);
    }

    private boolean verifyPassword(String password, String passwordReply) {
        return  (password != null && !password.isEmpty() && password.length() > 5 && password.equals(passwordReply));

    }

    public String generateTokenForUpdateUser(BookstoreUser user, String pass) {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact(user.getEmail());
        payload.setCode(pass);
        ContactConfirmationResponse response = jwtLogin(payload);
        return response.getResult();
    }

    public void authenticateUpdatedUser(BookstoreUser user, String token) {
            UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(user.getEmail());
            if (Boolean.TRUE.equals(jwtUtil.validateToken(token, userDetails))) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
