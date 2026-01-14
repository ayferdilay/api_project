package com.qa.project.service;

import com.qa.project.entity.Order;
import com.qa.project.entity.Product;
import com.qa.project.entity.User;
import com.qa.project.repository.OrderRepository;
import com.qa.project.repository.ProductRepository;
import com.qa.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("testuser");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setProducts(Collections.singletonList(testProduct));
        testOrder.setTotalPrice(100.0);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setStatus("PENDING");
    }

    @Test
    void getAllOrders_ShouldReturnOrderList() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(testOrder));

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getOrderById_WhenNotExists_ShouldThrowException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(99L));
        verify(orderRepository, times(1)).findById(99L);
    }

    @Test
    void createOrder_WhenValid_ShouldReturnSavedOrder() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findAllById(anyList())).thenReturn(Collections.singletonList(testProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.createOrder(1L, Collections.singletonList(1L));

        assertNotNull(result);
        assertEquals(100.0, result.getTotalPrice());
        assertEquals("PENDING", result.getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findAllById(anyList());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.createOrder(99L, Collections.singletonList(1L)));
        verify(productRepository, never()).findAllById(anyList());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WhenProductsNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> orderService.createOrder(1L, Collections.singletonList(99L)));
        verify(orderRepository, never()).save(any(Order.class));
    }
}
