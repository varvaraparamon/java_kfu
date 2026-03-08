package com.example.repositories;

import com.example.models.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long>{

    List<CartProduct> findByCartId(Long id);

}
