package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.entity.Cart;
import com.example.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}