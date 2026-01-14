package com.qa.project.repository;

import com.qa.project.entity.Category;
import com.qa.project.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByCategoryId_ShouldReturnProducts() {
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Tech");
        entityManager.persist(category);

        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setStock(5);
        product.setCategory(category);
        entityManager.persist(product);

        entityManager.flush();

        List<Product> products = productRepository.findByCategoryId(category.getId());

        assertEquals(1, products.size());
        assertEquals("Laptop", products.get(0).getName());
    }
}
