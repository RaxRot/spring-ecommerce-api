package com.raxrot.sproject.controller;

import com.raxrot.sproject.dto.ProductDTO;
import com.raxrot.sproject.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Operation(summary = "Get paged and sorted list of products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paged products")
    })
    @GetMapping("/public/products/paged")
    public ResponseEntity<List<ProductDTO>> getAllProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        List<ProductDTO> products = productService.findAllProductsPageable(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(products);
    }


    @Operation(summary = "Update product by ID")
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productId, productDTO));
    }

    @Operation(summary = "Update product image", description = "Updates the product image by uploading a new file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid file format or size")
    })
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(productService.updateProductImage(productId,image));
    }

    @Operation(summary = "Delete product by ID")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
