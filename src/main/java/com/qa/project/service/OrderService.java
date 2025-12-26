package com.qa.project.service;

import com.qa.project.entity.Order;
import com.qa.project.entity.Product;
import com.qa.project.entity.User;
import com.qa.project.repository.OrderRepository;
import com.qa.project.repository.ProductRepository;
import com.qa.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(Long userId, List<Long> productIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Product> products = productRepository.findAllById(productIds);

        if (products.isEmpty()) {
            throw new RuntimeException("No products found for the given IDs");
        }

        Double totalPrice = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }
}
