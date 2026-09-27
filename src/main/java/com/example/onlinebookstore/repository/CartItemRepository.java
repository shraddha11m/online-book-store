package com.example.onlinebookstore.repository;


import com.example.onlinebookstore.entity.Cart;
import com.example.onlinebookstore.entity.CartItem;
import com.example.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndBook(Cart cart, Book book);

    List<CartItem> findByCart(Cart cart);
}