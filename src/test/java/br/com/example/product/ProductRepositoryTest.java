package br.com.example.product;

import br.com.example.entities.Product;
import br.com.example.repositories.ProductRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    public void saveProduct(){
        Product productSaved = repository.save(getProduct());
        Assert.assertNotNull(productSaved);
    }

    @Test
    public void findPoductById() {
        Product productSaved = repository.save(getProduct());
        Optional<Product> result = repository.findById(productSaved.getId());

        Assert.assertEquals(productSaved, result.get());
    }

    @Test
    public void shoulReturnOptionalEmpty(){

        Assert.assertEquals(repository.findById(0), Optional.empty());
    }


    @Test
    public void findAllProducts(){

        Product product1 = new Product();
        product1.setDescription("Product description");
        product1.setName("Product 1");
        product1.setPrice(20.0);

        Product product2 = new Product();
        product2.setDescription("Product description 2");
        product2.setName("Product 2");
        product2.setPrice(30.0);

        this.repository.save(product1);
        this.repository.save(product2);

        Assert.assertEquals(2, this.repository.findAll(Pageable.unpaged()).getTotalElements());

    }


    private Product getProduct() {
        Product product = new Product();
        product.setDescription("Product description");
        product.setName("Television");
        product.setPrice(30.0);
        return  product;
    }

}
