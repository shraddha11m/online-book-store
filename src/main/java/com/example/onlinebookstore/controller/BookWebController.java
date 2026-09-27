package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookWebController {

    private final BookService bookService;

    public BookWebController(BookService bookService) {
        this.bookService = bookService;
    }

    // Home page - show all books
    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("books", bookService.getAllBooks());

        return "books";
    }


    // Search books
    @GetMapping("/search")
    public String searchBooks(
            @RequestParam String keyword,
            @RequestParam String type,
            Model model) {

        List<Book> books;

        if ("category".equalsIgnoreCase(type)) {

            books = bookService.searchByCategory(keyword);

        } else {

            books = bookService.searchByTitle(keyword);

        }

        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", type);

        return "books";
    }


    // View book details
    @GetMapping("/books/{id}")
    public String bookDetails(
            @PathVariable Long id,
            Model model) {

        Book book = bookService.getBookById(id);

        model.addAttribute("book", book);

        return "book-details";
    }
}
