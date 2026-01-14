package com.qa.project.repository;

import com.qa.project.entity.Order;
import com.qa.project.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findByUserId_ShouldReturnOrders() {
        User user = new User();
        user.setName("Order User");
        user.setEmail("order@example.com");
        user.setPassword("pass");
        entityManager.persist(user);

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(50.0);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        entityManager.persist(order);

        entityManager.flush();

        List<Order> orders = orderRepository.findByUserId(user.getId());

        assertEquals(1, orders.size());
        assertEquals(50.0, orders.get(0).getTotalPrice());
    }
}
