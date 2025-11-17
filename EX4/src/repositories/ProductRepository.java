package EX4.src.repositories;

import EX4.src.models.Product;

public interface ProductRepository {

    void save(Product product);

    Product findById(Integer id);

}
