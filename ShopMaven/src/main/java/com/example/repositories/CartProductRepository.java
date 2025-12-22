package com.example.repositories;

import java.util.List;

import com.example.models.CartProduct;

public interface CartProductRepository extends CrudRepository<CartProduct, Long>{

    List<CartProduct> findByCartId(Long id);

}
