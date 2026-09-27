package com.example.onlinebookstore.service;

import com.example.onlinebookstore.entity.*;
import com.example.onlinebookstore.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            BookRepository bookRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Order placeOrder(
            String username,
            String paymentMethod) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException(
                    "Cannot place order because cart is empty");
        }

        double totalAmount = 0;

        for (CartItem cartItem : cartItems) {

            Book book = cartItem.getBook();

            int quantity = cartItem.getQuantity();

            if (quantity <= 0) {
                throw new RuntimeException(
                        "Invalid quantity for book: "
                                + book.getTitle());
            }

            if (book.getStock() < quantity) {
                throw new RuntimeException(
                        "Not enough stock for book: "
                                + book.getTitle());
            }

            totalAmount +=
                    book.getPrice() * quantity;
        }

        Order order = new Order(
                user,
                totalAmount,
                "PLACED",
                paymentMethod
        );

        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {

            Book book = cartItem.getBook();

            OrderItem orderItem =
                    new OrderItem(
                            order,
                            book,
                            cartItem.getQuantity(),
                            book.getPrice()
                    );

            orderItemRepository.save(orderItem);

            book.setStock(
                    book.getStock()
                            - cartItem.getQuantity()
            );

            bookRepository.save(book);
        }

        cartItemRepository.deleteAll(cartItems);

        return order;
    }

    public List<Order> getOrders(String username) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return orderRepository.findByUser(user);
    }

    public Order getOrderByIdForUser(
            Long orderId,
            String username) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to view this order");
        }

        return order;
    }

    public List<OrderItem> getOrderItemsForUser(
            Long orderId,
            String username) {

        Order order =
                getOrderByIdForUser(
                        orderId,
                        username
                );

        return orderItemRepository
                .findByOrder(order);
    }

    @Transactional
    public void cancelOrder(
            Long orderId,
            String username) {

        Order order =
                getOrderByIdForUser(
                        orderId,
                        username
                );

        if ("CANCELLED".equalsIgnoreCase(
                order.getStatus())) {

            throw new RuntimeException(
                    "Order is already cancelled");
        }

        if (!"PLACED".equalsIgnoreCase(
                order.getStatus())) {

            throw new RuntimeException(
                    "This order cannot be cancelled");
        }

        List<OrderItem> orderItems =
                orderItemRepository
                        .findByOrder(order);

        for (OrderItem orderItem : orderItems) {

            Book book = orderItem.getBook();

            book.setStock(
                    book.getStock()
                            + orderItem.getQuantity()
            );

            bookRepository.save(book);
        }

        order.setStatus("CANCELLED");

        orderRepository.save(order);
    }
}