package EX4.src.repositories;

import EX4.src.models.Cart;

import java.util.HashMap;
import java.util.Map;

public class CartRepositoryMapImpl implements CartRepository{

    private Map<Long, Cart> carts;
    private Long currentId = 1L;

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
    public Cart findById(Long id) {
        return carts.get(id);
    }

    @Override
    public void update(Cart cart) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }


}
