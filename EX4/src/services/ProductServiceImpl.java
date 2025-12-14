package EX4.src.services;

import EX4.src.Logger;
import EX4.src.models.Product;
import EX4.src.repositories.ProductRepository;


public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
    private Logger logger;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
        this.logger = Logger.getLogger();
    }

    @Override
    public void create(Product product) {
        productRepository.save(product);
        logger.createProduct(product);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id);
    }
}
