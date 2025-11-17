package EX4.src.services;

import EX4.src.models.Cart;
import EX4.src.repositories.CartRepository;
import EX4.src.repositories.CartRepositoryMapImpl;

public class CartServiceImpl implements CartService{

    private CartRepository cartRepository;

    public CartServiceImpl(CartRepositoryMapImpl cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void create(Integer userId) {
        Cart cart = new Cart(userId);
        cartRepository.save(cart);
    }

    @Override
    public Cart getById(Integer id) {
        return cartRepository.findById(id);
    }

    @Override
    public void addProduct(Integer cartId, Integer productId, double price, int amount) {
        Cart cart = cartRepository.findById(cartId);
        if (cart == null) 
            return;
        cart.addProduct(productId, price, amount);
        cartRepository.save(cart); 
    }

    

}
