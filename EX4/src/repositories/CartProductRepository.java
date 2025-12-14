package EX4.src.repositories;

import EX4.src.models.CartProduct;

public interface CartProductRepository {
    void save(CartProduct cartProduct);

    CartProduct findById(Long id);
}
