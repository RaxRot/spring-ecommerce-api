package com.raxrot.sproject.service;

import com.raxrot.sproject.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDTO save(ProductDTO product, Long categoryId);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id, ProductDTO product); // <== тут
    void deleteProduct(Long id);

    List<ProductDTO>getProductsByCategoryId(Long categoryId);

    List<ProductDTO> searchProductByKeyword(String keyword);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
