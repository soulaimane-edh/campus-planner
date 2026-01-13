package com.example.campusplanner.controller;

import com.example.campusplanner.model.UserAccount;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserAdvice {

    @ModelAttribute("currentUser")
    public UserAccount currentUser(HttpSession session) {
        return (UserAccount) session.getAttribute("currentUser");
    }
}
