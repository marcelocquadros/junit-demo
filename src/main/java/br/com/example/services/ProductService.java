package br.com.example.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.example.entities.Product;

public interface ProductService {

    Page<Product> retrieveProducts(Pageable pageable);

    Product createProduct(Product product);

    Product findById(Integer id);
}
