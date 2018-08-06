package br.com.example.product;

import br.com.example.entities.Product;
import br.com.example.exceptions.ProductNotFoundException;
import br.com.example.repositories.ProductRepository;
import br.com.example.services.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void createProduct() {
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(getProduct());

        Assert.assertEquals(productService.createProduct(getProduct()), getProduct());
    }

    @Test
    public void findAllProducts(){

        Product product1 = new Product();
        product1.setId(1);
        product1.setDescription("Product description");
        product1.setName("Product 1");
        product1.setPrice(20.0);

        Product product2 = new Product();
        product2.setId(2);
        product2.setDescription("Product description 2");
        product2.setName("Product 2");
        product2.setPrice(30.0);

        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<Product>(products));

        Page<Product> result = productService.retrieveProducts(Pageable.unpaged());

        Assert.assertEquals(products, result.getContent());

    }


    @Test
    public void findById(){
        Product product = new Product();
        product.setDescription("Product description");
        product.setName("Television");
        product.setPrice(30.0);
        product.setId(10);

        Mockito.when(productRepository.findById(10)).thenReturn(Optional.of(product));

        Assert.assertEquals(productService.findById(10), product);
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldReturnProductNotFoundException(){
        this.productService.findById(1234);
    }

    private Product getProduct() {
        Product product = new Product();
        product.setId(10);
        product.setDescription("Product description");
        product.setName("Television");
        product.setPrice(30.0);
        return  product;
    }


}
