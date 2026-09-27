package com.example.onlinebookstore.service;
import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.entity.Cart;
import com.example.onlinebookstore.entity.CartItem;
import com.example.onlinebookstore.entity.User;
import com.example.onlinebookstore.repository.BookRepository;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.CartRepository;
import com.example.onlinebookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Cart addToCart(String username, Long bookId, int quantity) {

        // Find user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        // Check quantity
        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero");
        }

        // Check stock
        if (book.getStock() < quantity) {
            throw new RuntimeException(
                    "Not enough stock available");
        }

        // Find user's cart or create one
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() ->
                        cartRepository.save(new Cart(user))
                );

        // Check if book already exists in cart
        CartItem cartItem = cartItemRepository
                .findByCartAndBook(cart, book)
                .orElse(null);

        if (cartItem != null) {

            int newQuantity =
                    cartItem.getQuantity() + quantity;

            if (newQuantity > book.getStock()) {
                throw new RuntimeException(
                        "Requested quantity exceeds available stock");
            }

            cartItem.setQuantity(newQuantity);

        } else {

            cartItem = new CartItem(
                    cart,
                    book,
                    quantity
            );
        }

        cartItemRepository.save(cartItem);

        return cart;
    }

    public List<CartItem> getCartItems(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart is empty"));

        return cartItemRepository.findByCart(cart);
    }

    public CartItem updateQuantity(
            String username,
            Long bookId,
            int quantity) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero");
        }

        if (quantity > book.getStock()) {
            throw new RuntimeException(
                    "Not enough stock available");
        }

        CartItem cartItem = cartItemRepository
                .findByCartAndBook(cart, book)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Book is not present in cart"));

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(
            String username,
            Long bookId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndBook(cart, book)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Book is not present in cart"));

        cartItemRepository.delete(cartItem);
    }
}