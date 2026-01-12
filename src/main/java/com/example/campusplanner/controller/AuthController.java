package com.example.campusplanner.controller;

import com.example.campusplanner.model.UserAccount;
import com.example.campusplanner.repository.UserAccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserAccountRepository userAccountRepository;

    public AuthController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          HttpSession session,
                          Model model) {
        UserAccount account = userAccountRepository.findByEmail(email).orElse(null);
        if (account == null || !account.getPassword().equals(password) || !account.isActive()) {
            model.addAttribute("error", "Identifiants invalides");
            return "login";
        }
        session.setAttribute("currentUser", account);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
