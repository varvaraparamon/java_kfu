package EX4.src.services;

import EX4.src.Logger;
import EX4.src.models.Cart;
import EX4.src.models.CartProduct;
import EX4.src.repositories.CartProductRepository;
import EX4.src.repositories.CartRepository;

public class CartServiceImpl implements CartService{

    private CartRepository cartRepository;
    private CartProductRepository cartProductRepository;
    private Logger logger;

    public CartServiceImpl(CartRepository cartRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.logger = Logger.getLogger();
    }

    @Override
    public void create(Long userId) {
        Cart cart = new Cart(userId);
        cartRepository.save(cart);
        logger.createCart(cart);
    }

    @Override
    public Cart getById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public void addProduct(Long cartId, Long productId, Double price, Integer amount) {

        Cart cart = cartRepository.findById(cartId);
        if (cart == null)
            return;

        CartProduct cartProduct = new CartProduct(cartId, productId, amount);
        cartProductRepository.save(cartProduct);

        cart.addProduct(cartProduct.getId());
        cart.setCurrentSum(cart.getCurrentSum() + amount * price);

        cartRepository.update(cart); 
        logger.addProduct(productId);
    }

    

}
