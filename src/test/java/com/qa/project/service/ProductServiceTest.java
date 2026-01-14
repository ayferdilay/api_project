package com.qa.project.service;

import com.qa.project.entity.Category;
import com.qa.project.entity.Product;
import com.qa.project.repository.CategoryRepository;
import com.qa.project.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);
        testProduct.setStock(10);
        testProduct.setCategory(testCategory);
    }

    @Test
    void getAllProducts_ShouldReturnList() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_WhenNotExists_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(99L));
    }

    @Test
    void createProduct_WhenCategoryExists_ShouldReturnProduct() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_WhenCategoryNotExists_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.createProduct(testProduct));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenExists_ShouldReturnUpdatedProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Name");
        updatedDetails.setPrice(200.0);
        updatedDetails.setStock(20);

        Product result = productService.updateProduct(1L, updatedDetails);

        assertEquals("Updated Name", result.getName()); // Mock returns testProduct, but logic sets fields on it
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void deleteProduct_WhenExists_ShouldDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(testProduct);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(testProduct);
    }
}
