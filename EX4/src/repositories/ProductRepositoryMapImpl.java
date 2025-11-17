package EX4.src.repositories;

import EX4.src.models.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductRepositoryMapImpl implements ProductRepository{

    private Map<Integer, Product> products;
    private int currentId = 1;

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
    public Product findById(Integer id) {
        return products.get(id);
    }


}
