package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.entity.Order;
import com.example.onlinebookstore.service.OrderService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderService orderService;

    public OrderWebController(
            OrderService orderService) {

        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(
            Model model,
            Authentication authentication) {

        model.addAttribute(
                "username",
                authentication.getName()
        );

        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(
            @RequestParam String paymentMethod,
            Authentication authentication) {

        String username =
                authentication.getName();

        Order order =
                orderService.placeOrder(
                        username,
                        paymentMethod
                );

        return "redirect:/orders/"
                + order.getId();
    }

    @GetMapping
    public String myOrders(
            Model model,
            Authentication authentication) {

        String username =
                authentication.getName();

        model.addAttribute(
                "orders",
                orderService.getOrders(username)
        );

        return "orders";
    }

    @GetMapping("/{id}")
    public String orderDetails(
            @PathVariable Long id,
            Model model,
            Authentication authentication) {

        String username =
                authentication.getName();

        Order order =
                orderService.getOrderByIdForUser(
                        id,
                        username
                );

        model.addAttribute(
                "order",
                order
        );

        model.addAttribute(
                "orderItems",
                orderService.getOrderItemsForUser(
                        id,
                        username
                )
        );

        return "order-details";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(
            @PathVariable Long id,
            Authentication authentication) {

        String username =
                authentication.getName();

        orderService.cancelOrder(
                id,
                username
        );

        return "redirect:/orders/" + id;
    }
}