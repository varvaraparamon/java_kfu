package EX4.src.repositories;

import EX4.src.models.Cart;

public interface CartRepository {
    void save(Cart cart);

    Cart findById(Long id);

    void update(Cart cart);

}
