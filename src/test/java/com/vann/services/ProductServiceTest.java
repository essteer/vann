package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.models.enums.*;
import com.vann.repositories.ProductRepo;


class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindProductById() {
        Product product = new Product();
        UUID id = UUID.randomUUID();
        product.setName("Necklace");

        when(productRepo.findById(id)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findProductById(id);
        assertEquals("NECKLACE", foundProduct.getName());
        verify(productRepo, times(1)).findById(id);
    }

    @Test
    void testCreateOne_ValidProduct() {
        Category category = new Category(CategoryType.RING, "Jewellery");
        Product product = new Product(category, "Ring", 499.99, "image.png", Size.US_07, Colour.YELLOW_GOLD);
        when(categoryService.findCategoryByName(category.getName())).thenReturn(category);
        when(productRepo.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createOne(product);

        assertNotNull(createdProduct);
        assertEquals("RING", createdProduct.getName());
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateOne_InvalidProduct() {
        Category category = new Category(CategoryType.RING, "Jewellery");
        Product invalidProduct = new Product(category, null, -100.0, null, Size.US_07, Colour.YELLOW_GOLD);
    
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createOne(invalidProduct);
        });
    
        String expectedMessage = ProductService.class + " | ";
        assertTrue(exception.getMessage().startsWith(expectedMessage));
        assertTrue(exception.getMessage().contains("name cannot be null or empty"));
        assertTrue(exception.getMessage().contains("price must be greater than zero"));
        assertTrue(exception.getMessage().contains("imageURI cannot be null or empty"));
    
        verify(productRepo, times(0)).save(any(Product.class));
    }

    @Test
    void testCreateMany_ValidProducts() {
        List<Product> products = new ArrayList<>();
        Category category = new Category(CategoryType.RING, "Jewellery");
        products.add(new Product(category, "Ring", 499.99, "image.png", Size.US_07, Colour.YELLOW_GOLD, false));
        products.add(new Product(category, "Bracelet", 299.99, "image2.png", Size.US_07, Colour.WHITE_GOLD, true));

        when(categoryService.findCategoryByName(category.getName())).thenReturn(category);
        when(productRepo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Product> createdProducts = productService.createMany(products);

        assertEquals(2, createdProducts.size());
        verify(productRepo, times(2)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_ValidUpdate() {
        UUID id = UUID.randomUUID();
        Category existingCategory = new Category(CategoryType.RING, "Jewellery");
        Product existingProduct = new Product(existingCategory, "Old Necklace", 299.99, "image.png", Size.US_07, Colour.YELLOW_GOLD, false);
    
        when(productRepo.findById(id)).thenReturn(Optional.of(existingProduct));
        when(categoryService.findCategoryByName(anyString())).thenReturn(existingCategory);
        when(productRepo.save(any(Product.class))).thenReturn(existingProduct);
    
        Product updatedProduct = new Product();
        updatedProduct.setCategory(existingCategory);
        updatedProduct.setName("Updated Necklace");
        updatedProduct.setPrice(199.99);
        updatedProduct.setImageURI("image.png");
        updatedProduct.setSize(Size.US_08);
        updatedProduct.setColour(Colour.WHITE_GOLD);
        updatedProduct.setFeaturedStatus(true);
        Product result = productService.updateProduct(id, updatedProduct);
    
        assertEquals("UPDATED NECKLACE", result.getName());
        assertEquals(199.99, result.getPrice());
        verify(productRepo, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProduct_NotFound() {
        UUID id = UUID.randomUUID();
        when(productRepo.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            productService.updateProduct(id, new Product());
        });

        assertTrue(exception.getMessage().contains("record not found"));
    }

    @Test
    void testUpdateProduct_InvalidAttributes() {
        UUID id = UUID.randomUUID();
        Product existingProduct = new Product();
        existingProduct.setName("Old Necklace");
        when(productRepo.findById(id)).thenReturn(Optional.of(existingProduct));

        Product updatedProduct = new Product();
        updatedProduct.setName(null);
        updatedProduct.setPrice(-100.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(id, updatedProduct);
        });

        assertTrue(exception.getMessage().contains("name cannot be null or empty"));
        assertTrue(exception.getMessage().contains("price must be greater than zero"));
    }

    @Test
    void testDeleteProduct() {
        UUID id = UUID.randomUUID();
        when(productRepo.existsById(id)).thenReturn(true);
        doNothing().when(productRepo).deleteById(id);
        productService.deleteProduct(id);
        verify(productRepo, times(1)).deleteById(id);
    }
}