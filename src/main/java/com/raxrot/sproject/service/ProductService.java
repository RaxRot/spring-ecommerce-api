package com.raxrot.sproject.service;

import com.raxrot.sproject.dto.ProductDTO;
import com.raxrot.sproject.model.Product;

import java.util.List;

public interface ProductService {
    ProductDTO save(Product product, Long categoryId);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id, Product product); // <== тут
    void deleteProduct(Long id);

    List<ProductDTO>getProductsByCategoryId(Long categoryId);

    List<ProductDTO> searchProductByKeyword(String keyword);
}
