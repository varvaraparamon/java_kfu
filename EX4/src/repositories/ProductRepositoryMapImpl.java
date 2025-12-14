package EX4.src.repositories;

import EX4.src.models.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductRepositoryMapImpl implements ProductRepository{

    private Map<Long, Product> products;
    private Long currentId = 1L;

    public ProductRepositoryMapImpl(){
        this.products = new HashMap<>();
    }

    @Override
    public void save(Product product) {
        if (product.getId() == null) {
            product.setId(currentId++);
        }
        products.put(product.getId(), product);
    }

    @Override
    public Product findById(Long id) {
        return products.get(id);
    }


}
