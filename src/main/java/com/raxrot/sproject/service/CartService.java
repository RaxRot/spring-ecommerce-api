package com.raxrot.sproject.service;

import com.raxrot.sproject.dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    CartDTO updateProductQuantityInCart(Long productId, int delete);

    String deleteProductFromCart(Long cartId, Long productId);
}
