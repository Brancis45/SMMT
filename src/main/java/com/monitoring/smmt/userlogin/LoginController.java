package com.monitoring.smmt.userlogin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        return "login-page";
    }

    @GetMapping("/home")
    public String home() {
        return "home-page";
    }

    @GetMapping("/logout")
    public String logout() {
        return "login-page";
    }
}

