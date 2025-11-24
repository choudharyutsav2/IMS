package com.example.IMS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.IMS.entities.User;
import com.example.IMS.services.UserService;

@Controller
public class MyControllers {
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public User userModel() {
        return new User();
    }

    @GetMapping("/regPage")
    public String openRegPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/regForm")
    public String submitRegForm(@ModelAttribute("user") User user, Model model) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER"); // Assign default role
        }
        boolean status = userService.registerUser(user);
        if (status) {
            model.addAttribute("successMsg", "User registered successfully");
            return "redirect:/login.html";
        } else {
            model.addAttribute("errorMsg", "User not registered due to some error");
            return "register";
        }
    }

    @GetMapping("/login.html")
    public String openLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/loginForm")
    public String submitLoginForm(@ModelAttribute("user") User user, Model model) {
        User validUser = userService.loginUser(user.getEmail(), user.getPassword(), user.getRole());
        if (validUser != null) {
            if (validUser.getRole().equalsIgnoreCase("ADMIN")) {
                return "redirect:/adminPage"; // Redirect to admin page
            }
            return "redirect:/userPage?userId=" + validUser.getId(); // Redirect to inventory page for normal users
        } else {
            model.addAttribute("errorMsg", "Email ID and password didn't match!!");
            return "login";
        }
    }

    @GetMapping("/register.html")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Thymeleaf resolves to templates/register.html
    }

    @GetMapping("/update.html")
    public String showUpdatePage() {
        return "update";
    }

    @GetMapping("/addProduct.html")
    public String showaddProductPage() {
        return "addProduct";
    }

    @GetMapping("/InventoryPage")
    public String openInventoryPage(Model model) {
        return "inventory";
    }
}
