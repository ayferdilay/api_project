package com.qa.project.service;

import com.qa.project.entity.Product;
import com.qa.project.entity.Review;
import com.qa.project.entity.User;
import com.qa.project.repository.ProductRepository;
import com.qa.project.repository.ReviewRepository;
import com.qa.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private Product testProduct;
    private Review testReview;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");

        testReview = new Review();
        testReview.setId(1L);
        testReview.setText("Great product!");
        testReview.setRating(5);
        testReview.setUser(testUser);
        testReview.setProduct(testProduct);
    }

    @Test
    void getAllReviews_ShouldReturnReviewList() {
        when(reviewRepository.findAll()).thenReturn(Arrays.asList(testReview));

        List<Review> result = reviewService.getAllReviews();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void getReviewsByProduct_ShouldReturnReviewList() {
        when(reviewRepository.findByProductId(1L)).thenReturn(Arrays.asList(testReview));

        List<Review> result = reviewService.getReviewsByProduct(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findByProductId(1L);
    }

    @Test
    void addReview_WhenValid_ShouldReturnSavedReview() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        Review result = reviewService.addReview(1L, 1L, testReview);

        assertNotNull(result);
        assertEquals("Great product!", result.getText());
        verify(userRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void addReview_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reviewService.addReview(99L, 1L, testReview));
        verify(productRepository, never()).findById(any());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void addReview_WhenProductNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reviewService.addReview(1L, 99L, testReview));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void deleteReview_ShouldDelete() {
        doNothing().when(reviewRepository).deleteById(1L);

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }
}
