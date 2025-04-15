package com.raxrot.sproject.service;

import com.raxrot.sproject.dto.ProductDTO;
import com.raxrot.sproject.model.Category;
import com.raxrot.sproject.model.Product;
import com.raxrot.sproject.repository.CategoryRepository;
import com.raxrot.sproject.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              FileService fileService,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Product product = modelMapper.map(productDTO, Product.class);

        product.setCategory(category);

        product.setImage("default.png");
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() * 0.01);
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        List<Product>products=productRepository.findByCategoryOrderByPrice(category);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(keyword);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Upload image to server
        String path = "images/";
        String fileName = fileService.uploadImage(path, image);

        // Set new file name
        productFromDb.setImage(fileName);

        // Save
        Product updatedProduct = productRepository.save(productFromDb);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        existing.setProductName(product.getProductName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setDiscount(product.getDiscount());
        existing.setSpecialPrice(product.getPrice() - (product.getPrice() * product.getDiscount() * 0.01));

        Product updated = productRepository.save(existing);
        return modelMapper.map(updated, ProductDTO.class);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }
}
