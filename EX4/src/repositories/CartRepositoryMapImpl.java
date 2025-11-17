package EX4.src.repositories;

import EX4.src.models.Cart;

import java.util.HashMap;
import java.util.Map;

public class CartRepositoryMapImpl implements CartRepository{

    private Map<Integer, Cart> carts;
    private int currentId = 1;

    public CartRepositoryMapImpl(){
        this.carts = new HashMap<>();
    }

    @Override
    public void save(Cart cart) {
        if (cart.getId() == null) {
            cart.setId(currentId++);
        }
        carts.put(cart.getId(), cart);
    }

    @Override
    public Cart findById(Integer id) {
        return carts.get(id);
    }


}
