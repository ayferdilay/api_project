package com.qa.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.project.entity.Order;
import com.qa.project.entity.User;
import com.qa.project.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("testuser");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setTotalPrice(100.0);
        testOrder.setStatus("PENDING");
    }

    @Test
    void getAllOrders_ShouldReturnList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Arrays.asList(testOrder));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].totalPrice").value(100.0));
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(testOrder);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        when(orderService.createOrder(anyLong(), anyList())).thenReturn(testOrder);

        List<Long> productIds = Collections.singletonList(1L);

        mockMvc.perform(post("/api/orders")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productIds)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
