package com.raxrot.sproject.controller;

import com.raxrot.sproject.dto.ProductDTO;
import com.raxrot.sproject.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Product Controller", description = "Product CRUD operations, search, filtering by category")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a new product in a category")
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                                    @PathVariable Long categoryId) {
        ProductDTO saved = productService.save(productDTO, categoryId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all products")
    @GetMapping("/public/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/public/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Operation(summary = "Get all products by category")
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId));
    }

    @Operation(summary = "Search products by keyword")
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<List<ProductDTO>> getAllProductsByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(productService.searchProductByKeyword(keyword));
    }

    @Operation(summary = "Update product by ID")
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productId, productDTO));
    }

    @Operation(summary = "Delete product by ID")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
