package com.qa.project.repository;

import com.qa.project.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByName_ShouldReturnCategory() {
        Category category = new Category();
        category.setName("Unique Category");
        category.setDescription("Desc");
        entityManager.persist(category);
        entityManager.flush();

        Optional<Category> found = categoryRepository.findByName("Unique Category");

        assertTrue(found.isPresent());
        assertEquals("Unique Category", found.get().getName());
    }
}
