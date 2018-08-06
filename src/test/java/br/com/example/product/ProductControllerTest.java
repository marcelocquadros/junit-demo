package br.com.example.product;

import br.com.example.controllers.ProductController;
import br.com.example.entities.Product;
import br.com.example.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class, secure = false)
@EnableSpringDataWebSupport
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;


    @Test
    public void createProduct() throws Exception {

        Product product = new Product();
        product.setId(10);
        product.setName("Product one");
        product.setPrice(20.0);
        product.setDescription("Prod description");

        Mockito.when(productService.createProduct(Mockito.any(Product.class))).thenReturn(product);

        String URI = "/v1/products";

        String inputInJson =  mapToJson(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }


    @Test
    public void retrieveProduct() throws Exception {

        Product product = new Product();
        product.setId(10);
        product.setName("Product one");
        product.setPrice(20.0);
        product.setDescription("Prod description");

        Mockito.when(productService.findById(10)).thenReturn(product);

        String URI = "/v1/products/10";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expectedJson = this.mapToJson(product);

        String outputInJson = result.getResponse().getContentAsString();

        Assert.assertEquals(expectedJson, outputInJson);

    }


    @Test
    public void retrieveProducts() throws Exception{

        Product product = new Product();
        product.setId(10);
        product.setName("Product one");
        product.setPrice(10.0);
        product.setDescription("Prod description");

        Product product2 = new Product();
        product2.setId(20);
        product2.setName("Product two");
        product2.setPrice(20.0);
        product2.setDescription("Prod description2");

        Page<Product> productsPage = new PageImpl<Product>(Arrays.asList(product, product2));

        Mockito.when(productService.retrieveProducts(Mockito.any(Pageable.class)))
                .thenReturn(productsPage);

        String URI = "/v1/products";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .accept(MediaType.APPLICATION_JSON);


        MvcResult mockResult = mockMvc.perform(requestBuilder).andReturn();

        String expectedJson = mapToJson(productsPage);

        String outputJson  = mockResult.getResponse().getContentAsString();

        Assert.assertEquals(expectedJson, outputJson);

    }


    /**
     * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
     */
    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
