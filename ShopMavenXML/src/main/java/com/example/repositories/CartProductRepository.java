package com.example.repositories;

import com.example.models.CartProduct;
import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends CrudRepository<CartProduct, Long> {
    List<CartProduct> findByCartId(Long id);
    Optional<CartProduct> findByCartIdAndProductId(Long cartId, Long productId);
    void deleteByCartId(Long cartId);
}
