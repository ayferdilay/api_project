package com.qa.project.repository;

import com.qa.project.entity.Category;
import com.qa.project.entity.Product;
import com.qa.project.entity.Review;
import com.qa.project.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void findByProductId_ShouldReturnReviews() {
        Category category = new Category();
        category.setName("Review Cat");
        category.setDescription("Desc");
        entityManager.persist(category);

        Product product = new Product();
        product.setName("Review Prod");
        product.setPrice(10.0);
        product.setStock(1);
        product.setCategory(category);
        entityManager.persist(product);

        User user = new User();
        user.setName("Reviewer");
        user.setEmail("rev@example.com");
        user.setPassword("pass");
        entityManager.persist(user);

        Review review = new Review();
        review.setText("Good");
        review.setRating(5);
        review.setUser(user);
        review.setProduct(product);
        entityManager.persist(review);

        entityManager.flush();

        List<Review> reviews = reviewRepository.findByProductId(product.getId());

        assertEquals(1, reviews.size());
        assertEquals("Good", reviews.get(0).getText());
    }
}
