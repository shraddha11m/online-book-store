package com.example.onlinebookstore.controller;
import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {

        Book savedBook = bookService.addBook(book);

        return ResponseEntity.ok(savedBook);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book book) {

        return ResponseEntity.ok(
                bookService.updateBook(id, book)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.ok("Book deleted successfully");
    }

    // SEARCH BY TITLE
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchByTitle(
            @RequestParam String title) {

        return ResponseEntity.ok(
                bookService.searchByTitle(title)
        );
    }

    // SEARCH BY CATEGORY
    @GetMapping("/search/category")
    public ResponseEntity<List<Book>> searchByCategory(
            @RequestParam String category) {

        return ResponseEntity.ok(
                bookService.searchByCategory(category)
        );
    }
}