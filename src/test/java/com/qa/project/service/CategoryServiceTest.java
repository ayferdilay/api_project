package com.qa.project.service;

import com.qa.project.entity.Category;
import com.qa.project.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setDescription("Devices and gadgets");
    }

    @Test
    void getAllCategories_ShouldReturnCategoryList() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory));

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_WhenExists_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        Category result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategoryById_WhenNotExists_ShouldThrowException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void createCategory_ShouldReturnSavedCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        Category result = categoryService.createCategory(testCategory);

        assertNotNull(result);
        assertEquals(testCategory.getName(), result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_WhenExists_ShouldReturnUpdatedCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        Category categoryDetails = new Category();
        categoryDetails.setName("Updated Electronics");
        categoryDetails.setDescription("Updated description");

        Category result = categoryService.updateCategory(1L, categoryDetails);

        assertEquals("Updated Electronics", result.getName());
        verify(categoryRepository, times(1)).save(testCategory);
    }

    @Test
    void deleteCategory_WhenExists_ShouldDelete() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        doNothing().when(categoryRepository).delete(testCategory);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(testCategory);
    }
}
