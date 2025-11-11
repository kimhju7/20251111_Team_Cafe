package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "MSA 프로젝트에 오신 것을 환영합니다");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "로그인");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "회원 가입");
        return "register";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("message", "게시글 목록");
        return "post";
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable String id, Model model) {
        model.addAttribute("message", "게시글 상세");
        return "detail";
    }

    @GetMapping("/categorys")
    public String products(Model model) {
        model.addAttribute("message", "카테고리 목록");
        return "categorys";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("message", "관리자 페이지");
        return "admin";
    }

    @GetMapping("/customers")
    public String customer(Model model) {
        model.addAttribute("message", "고객 목록");
        return "customers";
    }

    @GetMapping("/menus")
    public String menu(Model model) {
        model.addAttribute("message", "카페 메뉴");
        return "menus";
    }

    @GetMapping("/reviews")
    public String reviews(Model model) {
        model.addAttribute("Message", "리뷰");
        return "reviews";
    }
} 