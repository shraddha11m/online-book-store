package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.entity.Order;
import com.example.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}