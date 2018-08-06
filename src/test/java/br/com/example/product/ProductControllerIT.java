package br.com.example.product;

import br.com.example.JUnitDemoApplication;
import br.com.example.entities.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JUnitDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void createProduct() {

        Product product = new Product();
        product.setName("Product one");
        product.setDescription("Description one");
        product.setPrice(40.0);

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                getUrl("/v1/products"), product, Void.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void retrieveProduct() {

        Product product = new Product();
        product.setName("Product one");
        product.setDescription("Description one");
        product.setPrice(40.0);

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                getUrl("v1/products"), product, Void.class);

        String location = response.getHeaders().getLocation().getPath();

        ResponseEntity<Product> productFound = testRestTemplate.getForEntity(location, Product.class);

        Assert.assertNotNull(productFound);

    }

    @Test
    public void retrieveProducts() {

        Product product = new Product();
        product.setName("Product one");
        product.setDescription("Description one");
        product.setPrice(40.0);

        testRestTemplate.postForEntity(
                getUrl("/v1/products"), product, Void.class);

        Product product2 = new Product();
        product2.setName("Product two");
        product2.setDescription("Description two");
        product2.setPrice(40.0);

        testRestTemplate.postForEntity(
                getUrl("/v1/products"), product, Void.class);

        String jsonResponse = testRestTemplate.getForObject(getUrl("/v1/products"), String.class);
        String expected = "\"totalElements\":2,";
        Assert.assertTrue(jsonResponse.contains(expected));

    }

    @Test
    public void shouldReturn404(){
        ResponseEntity<Product> response = testRestTemplate.getForEntity(
                getUrl("/v1/products/909090"), Product.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    private String getUrl(String path) {
        return String.format("http://localhost:%s%s", port, path);
    }

}