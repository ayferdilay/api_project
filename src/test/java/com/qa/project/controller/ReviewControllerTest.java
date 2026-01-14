package com.qa.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.project.entity.Product;
import com.qa.project.entity.Review;
import com.qa.project.entity.User;
import com.qa.project.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private Review testReview;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        Product testProduct = new Product();
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
    void getAllReviews_ShouldReturnList() throws Exception {
        when(reviewService.getAllReviews()).thenReturn(Arrays.asList(testReview));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void getReviewsByProduct_ShouldReturnList() throws Exception {
        when(reviewService.getReviewsByProduct(1L)).thenReturn(Arrays.asList(testReview));

        mockMvc.perform(get("/api/reviews/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void addReview_ShouldReturnCreated() throws Exception {
        when(reviewService.addReview(eq(1L), eq(1L), any(Review.class))).thenReturn(testReview);

        mockMvc.perform(post("/api/reviews")
                .param("userId", "1")
                .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReview)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteReview_ShouldReturnNoContent() throws Exception {
        doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }
}
