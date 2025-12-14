package EX4.src.services;

import EX4.src.models.Cart;

public interface CartService {
    void create(Long userId);
    Cart getById(Long id);
    void addProduct(Long cartId, Long productId, Double price, Integer amount);
}
