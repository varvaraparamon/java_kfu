package EX4.src.repositories;

import java.util.HashMap;
import java.util.Map;

import EX4.src.models.CartProduct;

public class CartProductRepositoryMapImpl implements CartProductRepository{

    private Map<Long, CartProduct> cartProducts;
    private Long currentId = 1L;

    public CartProductRepositoryMapImpl(){
        this.cartProducts = new HashMap<>();
    }

    @Override
    public void save(CartProduct cartProduct) {
        if (cartProduct.getId() == null) {
            cartProduct.setId(currentId++);
        }
        cartProducts.put(cartProduct.getId(), cartProduct);
    }

    @Override
    public CartProduct findById(Long id) {
        return cartProducts.get(id);
    }

}
