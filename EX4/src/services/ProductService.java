package EX4.src.services;

import EX4.src.models.Product;

public interface ProductService {
    void create(Product product);
    Product getById(Long id);
}
