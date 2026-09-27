package com.example.onlinebookstore.controller;
import com.example.onlinebookstore.entity.Cart;
import com.example.onlinebookstore.entity.CartItem;
import com.example.onlinebookstore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestParam String username,
            @RequestParam Long bookId,
            @RequestParam int quantity) {

        Cart cart = cartService.addToCart(
                username,
                bookId,
                quantity
        );

        return ResponseEntity.ok(cart);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestParam String username) {

        return ResponseEntity.ok(
                cartService.getCartItems(username)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<CartItem> updateQuantity(
            @RequestParam String username,
            @RequestParam Long bookId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        username,
                        bookId,
                        quantity
                )
        );
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(
            @RequestParam String username,
            @RequestParam Long bookId) {

        cartService.removeFromCart(username, bookId);

        return ResponseEntity.ok(
                "Book removed from cart successfully"
        );
    }
}