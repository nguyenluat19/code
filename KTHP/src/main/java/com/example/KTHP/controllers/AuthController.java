package com.example.KTHP.controllers;

import com.example.KTHP.models.User;
import com.example.KTHP.reponsitory.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register() {
        return "auth/register";
    }

    @Transactional
    @PostMapping("/auth/register")
    public String registerUser(User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại");
            return "auth/register";
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/auth/login";
    }

    @PostMapping("/auth/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null && passwordEncoder.matches(password, existingUser.getPassword())) {
            // Đăng nhập thành công
            return "redirect:/products"; // Chuyển hướng đến trang sản phẩm
        }

        model.addAttribute("error", "Thông tin đăng nhập không hợp lệ");
        return "auth/login"; // Quay lại trang đăng nhập nếu thông tin không hợp lệ
    }
}
