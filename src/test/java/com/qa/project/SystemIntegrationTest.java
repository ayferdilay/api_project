package com.qa.project;

import com.qa.project.entity.*;
import com.qa.project.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SystemIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void shouldCompleteFullECommerceFlow() {
        // 1. Create Category
        Category category = new Category();
        category.setName("Integration Category");
        category.setDescription("Integration Description");
        categoryRepository.save(category);

        // 2. Create Product
        Product product = new Product();
        product.setName("Integration Product");
        product.setPrice(100.0);
        product.setStock(50);
        product.setCategory(category);
        productRepository.save(product);

        // 3. Create User
        User user = new User();
        user.setName("Integration User");
        user.setEmail("integrated@example.com");
        user.setPassword("securepass");
        userRepository.save(user);

        // 4. Create Order
        Order order = new Order();
        order.setUser(user);
        order.setProducts(Collections.singletonList(product));
        order.setTotalPrice(100.0);
        order.setStatus("CONFIRMED");
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        // 5. Create Review
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(5);
        review.setText("Excellent integration!");
        reviewRepository.save(review);

        // --- Verification ---

        // Verify Order
        List<Order> userOrders = orderRepository.findByUserId(user.getId());
        assertEquals(1, userOrders.size());
        assertEquals("CONFIRMED", userOrders.get(0).getStatus());
        assertEquals("Integration Product", userOrders.get(0).getProducts().get(0).getName());

        // Verify Review
        List<Review> productReviews = reviewRepository.findByProductId(product.getId());
        assertEquals(1, productReviews.size());
        assertEquals("Excellent integration!", productReviews.get(0).getText());
        assertEquals("Integration User", productReviews.get(0).getUser().getName());
    }

    @Test
    void shouldManageProductLifecycle() {
        // 1. Create Category
        Category category = new Category();
        category.setName("Lifecycle Cat");
        categoryRepository.save(category);

        // 2. Create Product
        Product product = new Product();
        product.setName("Original Product");
        product.setPrice(50.0);
        product.setStock(10);
        product.setCategory(category);
        productRepository.save(product);

        // 3. Update Product
        product.setName("Updated Product");
        product.setPrice(75.0);
        productRepository.save(product);

        // Verify Update
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(75.0, updatedProduct.getPrice());

        // 4. Delete Product
        productRepository.delete(product);

        // Verify Deletion
        assertTrue(productRepository.findById(product.getId()).isEmpty());
    }

    @Test
    void shouldManageUserLifecycle() {
        // 1. Create User
        User user = new User();
        user.setName("Lifecycle User");
        user.setEmail("lifecycle@example.com");
        user.setPassword("pass123");
        userRepository.save(user);

        // 2. Update User
        user.setName("Updated User Name");
        userRepository.save(user);

        // Verify Update
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("Updated User Name", updatedUser.getName());

        // 3. Delete User
        userRepository.delete(user);

        // Verify Deletion
        assertTrue(userRepository.findById(user.getId()).isEmpty());
    }

    @Test
    void shouldFilterProductsByCategory() {
        // 1. Create Categories
        Category catA = new Category();
        catA.setName("Category A");
        categoryRepository.save(catA);

        Category catB = new Category();
        catB.setName("Category B");
        categoryRepository.save(catB);

        // 2. Add Products
        Product prodA1 = new Product();
        prodA1.setName("Product A1");
        prodA1.setCategory(catA);
        prodA1.setStock(10);
        prodA1.setPrice(10.0);
        productRepository.save(prodA1);

        Product prodB1 = new Product();
        prodB1.setName("Product B1");
        prodB1.setCategory(catB);
        prodB1.setStock(10);
        prodB1.setPrice(10.0);
        productRepository.save(prodB1);

        // 3. Filter by Category A
        List<Product> productsInA = productRepository.findByCategoryId(catA.getId());

        // Verify
        assertEquals(1, productsInA.size());
        assertEquals("Product A1", productsInA.get(0).getName());
    }

    @Test
    void shouldManageReviews() {
        // Setup Prereqs
        Category cat = new Category();
        cat.setName("Review Cat");
        categoryRepository.save(cat);

        Product product = new Product();
        product.setName("Reviewable Product");
        product.setCategory(cat);
        product.setStock(10);
        product.setPrice(10.0);
        productRepository.save(product);

        User user = new User();
        user.setName("Reviewer");
        user.setEmail("reviewer@example.com");
        user.setPassword("pass");
        userRepository.save(user);

        // 1. Add Review
        Review review1 = new Review();
        review1.setText("First Review");
        review1.setRating(4);
        review1.setUser(user);
        review1.setProduct(product);
        reviewRepository.save(review1);

        // 2. Verify Review Exists
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        assertEquals(1, reviews.size());

        // 3. Delete Review
        reviewRepository.delete(review1);

        // 4. Verify Deletion
        List<Review> emptyReviews = reviewRepository.findByProductId(product.getId());
        assertTrue(emptyReviews.isEmpty());
    }
}
