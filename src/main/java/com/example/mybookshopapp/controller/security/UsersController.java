package com.example.mybookshopapp.controller.security;

import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.Role;
import com.example.mybookshopapp.entity.security.UserContact;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import com.example.mybookshopapp.service.security.BookstoreUserService;
import com.example.mybookshopapp.service.security.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {

    private static final String USERS_SLUG = "users/slug";
    private static final String REDIRECT_USERS = "redirect:/users/";

    private final BookstoreUserRepository bookstoreUserRepository;
    private final BookstoreUserService bookstoreUserService;
    private final UserContactService userContactService;

    @Autowired
    public UsersController(BookstoreUserRepository bookstoreUserRepository, BookstoreUserService bookstoreUserService, UserContactService userContactService) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.bookstoreUserService = bookstoreUserService;
        this.userContactService = userContactService;
    }

    @GetMapping("")
    public String usersPage(Model model) {
        model.addAttribute("users", bookstoreUserService.getPageOfUsers(0, 20));
        return "users/users";
    }

    @GetMapping("/{hash}")
    public String userSlugPage(@PathVariable String hash, Model model) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(hash);
        if (user.getRoles().contains(Role.ANONYMOUS)) {
            model.addAttribute("warnMessage", "User is not registered");
        } else {
            List<UserContact> userContacts = userContactService.getContactsByUser(user);
            if (userContacts.get(0).getCodeTime().minusMinutes(5).isAfter(LocalDateTime.now())) {
                Duration duration = Duration.between(LocalDateTime.now(), userContacts.get(0).getCodeTime());
                String messageBlock = "User is blocked, left " +
                        (duration.toHours() < 1 ? duration.toMinutes() + " minutes" : duration.toHours() + " hours");

                model.addAttribute("blocked", messageBlock);
            }
            model.addAttribute("user", user);
            model.addAttribute("contacts", userContacts);
        }
        return USERS_SLUG;
    }

    @GetMapping("/remove/{hash}")
    public String removeUser(@PathVariable String hash, Model model) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(hash);
        bookstoreUserRepository.delete(user);
        model.addAttribute("warnMessage", "User removed");
        return USERS_SLUG;
    }

    @GetMapping("/{hash}/remove/role")
    public String removeRoleAdmin(@PathVariable String hash, RedirectAttributes redirectAttributes) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(hash);
        user.getRoles().remove(Role.ADMIN);
        user = bookstoreUserRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Role ADMIN removed from user " + user.getName());
        return REDIRECT_USERS + hash;
    }

    @GetMapping("/{hash}/add/role")
    public String addRoleAdmin(@PathVariable String hash, RedirectAttributes redirectAttributes) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(hash);
        user.getRoles().add(Role.ADMIN);
        bookstoreUserRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Role ADMIN added to user " + user.getName());
        return REDIRECT_USERS + hash;
    }

    @GetMapping("/list")
    @ResponseBody
    public List<BookstoreUser> getUsersList(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
       return bookstoreUserService.getPageOfUsers(offset, limit);
    }

    @GetMapping("/{hash}/block/{hours}")
    public String blockAccessUser(@PathVariable String hash, @PathVariable Integer hours) {
        bookstoreUserService.blockAccessUserFor(hash, hours);
        return REDIRECT_USERS + hash;
    }
}
