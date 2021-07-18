package com.example.mybookshopapp.service.security;

import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.ContactType;
import com.example.mybookshopapp.entity.security.UserContact;
import com.example.mybookshopapp.errs.security.NotFoundUserWithContactException;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserContactService {

    @Value("${twilio.ACCOUNT_SID}")
    private String accountSid;

    @Value("${twilio.AUTH_TOKEN}")
    private String authToken;

    @Value("${twilio.TWILIO_NUMBER}")
    private String twilioNumber;

    private Random random = new Random();

    private final UserContactRepository userContactRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserContactService(UserContactRepository userContactRepository, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.userContactRepository = userContactRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    public void sendSecretCodeSms(String contact, String code) {
        Twilio.init(accountSid, authToken);
        String formattedContact = contact.replaceAll("[( )-]", "");
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(twilioNumber),
                "Your secret code is: " + code
        ).create();
    }

    public String generateCode() {
        //nnn nnn - pattern
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(random.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

    public void saveNewCode(String code, String contact) throws NotFoundUserWithContactException {
        UserContact userContact = userContactRepository.findByContact(contact);
        if (userContact == null || userContact.getApproved() == 0) {
            throw new NotFoundUserWithContactException("User with specified contact is not exists");
        }
        userContact.setCode(passwordEncoder.encode(code));
        userContact.setCodeTime(LocalDateTime.now());
        userContact.setCodeTrials(0);
        userContactRepository.save(userContact);
    }


    public void saveNewCodeForReg(String code, String contact) {
        UserContact userContact = new UserContact(code, contact);
        if (contact.contains("@")) {
            userContact.setType(ContactType.EMAIL);
        } else {
            userContact.setType(ContactType.PHONE);
        }
        userContactRepository.save(userContact);
    }

    public Boolean verifyCode(String contact, String code) {
        UserContact userContact = userContactRepository.findByContact(contact);
        if (userContact == null || !userContact.getCode().equals(code)) {
            return false;
        }
        LocalDateTime timeCode = userContact.getCodeTime();
        if (userContact.getType().equals(ContactType.EMAIL)) {
            return LocalDateTime.now().plusSeconds(300).isAfter(timeCode);
        }
        if (userContact.getType().equals(ContactType.PHONE)) {
            return LocalDateTime.now().plusSeconds(60).isAfter(timeCode);
        }
        return false;
    }

    public Boolean verifyCode(BookstoreUser user, String contact, String code) {
        UserContact userContact = userContactRepository.findByUserAndContact(user, contact);
        if (userContact == null || !userContact.getCode().equals(code)) {
            return false;
        }
        return LocalDateTime.now().plusSeconds(300).isAfter(userContact.getCodeTime());
    }

    public UserContact getByEmail(String email) {
        UserContact contactByEmail = userContactRepository.findByContact(email);
        if (contactByEmail == null || !contactByEmail.getType().equals(ContactType.EMAIL)) {
            return null;
        }
        return contactByEmail;
    }

    public UserContact getByPhone(String phone) {
        UserContact contactByEmail = userContactRepository.findByContact(phone);
        if (contactByEmail == null || !contactByEmail.getType().equals(ContactType.PHONE)) {
            return null;
        }
        return contactByEmail;
    }

    public void sendVerificationEmail(String emailFrom, String emailTo, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setSubject("Bookstore email verification!");
        message.setText("Verification code is: " + code);
        javaMailSender.send(message);
    }
}
