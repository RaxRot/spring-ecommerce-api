package com.raxrot.sproject.repository;

import com.raxrot.sproject.model.Category;
import com.raxrot.sproject.model.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        category = new Category();
        category.setCategoryName("Test Category");
        category = categoryRepository.save(category);
    }

    @DisplayName("Test add product to Db")
    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setProductName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(1500.0);
        product.setQuantity(5);
        product.setDiscount(10.0);
        product.setImage("default.png");
        product.setSpecialPrice(1350.0);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
        assertThat(savedProduct.getCategory().getCategoryName()).isEqualTo("Test Category");
    }

    @DisplayName("Test get all products")
    @Test
    void testGetAllProducts() {
        Product product1 = new Product(
                null,
                "Phone",
                "phone.png",
                "Smartphone",
                10,
                800.0,
                5.0,
                760.0,
                category
        );
        Product product2 = new Product(
                null,
                "Phone2",
                "phone2.png",
                "Smartphone2",
                20,
                200.0,
                2.0,
                260.0,
                category
        );
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
    }

    @DisplayName("Test update product")
    @Test
    void testUpdateProduct() {
        Product product = new Product(
                null,
                "Phone",
                "phone.png",
                "Smartphone",
                10,
                800.0,
                5.0,
                760.0,
                category
        );
        product = productRepository.save(product);

        Product found = productRepository.findById(product.getId()).get();
        found.setProductName("Updated Watch");
        found.setPrice(180.0);
        Product updated = productRepository.save(found);

        assertThat(updated.getProductName()).isEqualTo("Updated Watch");
        assertThat(updated.getPrice()).isEqualTo(180.0);
    }

    @DisplayName("Test delete product")
    @Test
    void testDeleteProduct() {
        Product product = new Product(
                null,
                "Phone",
                "phone.png",
                "Smartphone",
                10,
                800.0,
                5.0,
                760.0,
                category
        );
        product = productRepository.save(product);

        productRepository.deleteById(product.getId());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }
}
