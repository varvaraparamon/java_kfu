package EX4.src.services;

import EX4.src.models.Cart;

public interface CartService {
    void create(Integer userId);
    Cart getById(Integer id);
    void addProduct(Integer cartId, Integer productId, double price, int amount);
}
