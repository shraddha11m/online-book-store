package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartWebController {

    private final CartService cartService;

    public CartWebController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long bookId,
            @RequestParam int quantity,
            Authentication authentication) {

        String username = authentication.getName();

        cartService.addToCart(
                username,
                bookId,
                quantity
        );

        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(
            Model model,
            Authentication authentication) {

        String username = authentication.getName();

        model.addAttribute(
                "cartItems",
                cartService.getCartItems(username)
        );

        return "cart";
    }

    @PostMapping("/update")
    public String updateCart(
            @RequestParam Long bookId,
            @RequestParam int quantity,
            Authentication authentication) {

        String username = authentication.getName();

        cartService.updateQuantity(
                username,
                bookId,
                quantity
        );

        return "redirect:/cart";
    }
}