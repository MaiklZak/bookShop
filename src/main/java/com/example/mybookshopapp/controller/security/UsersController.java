package com.example.mybookshopapp.controller.security;

import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.Role;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UsersController {

    private static final String REDIRECT_USERS_URL = "redirect:/users";
    private static final String MESSAGE = "message";

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public UsersController(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @GetMapping("")
    public String usersPage(Model model) {
        model.addAttribute("users", bookstoreUserRepository.findAll());
        return "users/users";
    }

    @GetMapping("/remove/{hash}")
    public String removeUser(@PathVariable String hash, RedirectAttributes redirectAttributes) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(hash);
        String name = user.getName();
        bookstoreUserRepository.delete(user);
        redirectAttributes.addFlashAttribute(MESSAGE, "User with name: " + name + " removed");
        return REDIRECT_USERS_URL;
    }

    @GetMapping("/{hash}/remove/role")
    public String removeRoleAdmin(@PathVariable String hash, RedirectAttributes redirectAttributes) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(hash);
        user.getRoles().remove(Role.ADMIN);
        user = bookstoreUserRepository.save(user);
        redirectAttributes.addFlashAttribute(MESSAGE, "Role ADMIN removed from user " + user.getName());
        return REDIRECT_USERS_URL;
    }

    @GetMapping("/{hash}/add/role")
    public String addRoleAdmin(@PathVariable String hash, RedirectAttributes redirectAttributes) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(hash);
        user.getRoles().add(Role.ADMIN);
        bookstoreUserRepository.save(user);
        redirectAttributes.addFlashAttribute(MESSAGE, "Role ADMIN added to user " + user.getName());
        return REDIRECT_USERS_URL;
    }
}
