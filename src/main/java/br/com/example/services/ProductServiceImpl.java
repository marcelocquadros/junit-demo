package br.com.example.services;

import br.com.example.entities.Product;
import br.com.example.exceptions.ProductNotFoundException;
import br.com.example.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ProductServiceImpl implements  ProductService{

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> retrieveProducts(Pageable pageable) {

        logger.info("Retrieve products ");
        Page<Product> products = this.productRepository.findAll(pageable);
        logger.info("Retrieved {} products", products.getTotalElements());
        return products;
    }


    @Override
    public Product createProduct(final Product product) {
        if(isNull(product) ) {
            logger.error("product is null");
            throw new IllegalArgumentException("productId is null");
        }
        logger.info("Creating product {}", product);
        product.setId(null); //dismiss id case informed on create
        logger.info("Saving product");
        Product productSaved = this.productRepository.save(product);
        logger.info("Product saved {}", product);
        return productSaved;
    }

    @Override
    public Product findById(final Integer productId) {
        if( productId == null) {
            logger.error("productId is null");
            throw new IllegalArgumentException("productId is null");
        }
        Optional<Product> product = productRepository.findById(productId);
        if(! product.isPresent() ) {
            logger.warn("Product id {}  not found", productId);
            throw new ProductNotFoundException("Product id [ "+ productId +" ] not found");
        }

        logger.info("Product found {} ", product.get());

        return product.get();
    }

}
