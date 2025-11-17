package EX4.src.services;

import EX4.src.models.Product;

import EX4.src.repositories.ProductRepository;
import EX4.src.repositories.ProductRepositoryMapImpl;


public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepositoryMapImpl productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public void create(Product product) {
        productRepository.save(product);
    }

    @Override
    public Product getById(Integer id) {
        return productRepository.findById(id);
    }
}
