package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.service.BookService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final BookService bookService;

    public AdminWebController(
            BookService bookService) {

        this.bookService = bookService;
    }

    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute(
                "books",
                bookService.getAllBooks()
        );

        return "admin-dashboard";
    }

    @GetMapping("/books/add")
    public String showAddBookForm(
            Model model) {

        model.addAttribute(
                "book",
                new Book()
        );

        return "admin-add-book";
    }

    @PostMapping("/books/add")
    public String addBook(
            @Valid @ModelAttribute("book") Book book,
            BindingResult result) {

        if (result.hasErrors()) {
            return "admin-add-book";
        }

        bookService.addBook(book);

        return "redirect:/admin";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditBookForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "book",
                bookService.getBookById(id)
        );

        return "admin-edit-book";
    }

    @PostMapping("/books/edit/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute("book") Book book,
            BindingResult result) {

        if (result.hasErrors()) {
            return "admin-edit-book";
        }

        bookService.updateBook(id, book);

        return "redirect:/admin";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return "redirect:/admin";
    }
}