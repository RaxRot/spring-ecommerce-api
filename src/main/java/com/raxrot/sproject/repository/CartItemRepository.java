package com.raxrot.sproject.repository;

import com.raxrot.sproject.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProduct_IdAndCart_CartId(Long productId, Long cartId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.cartId = :cartId AND c.product.id = :productId")
    void deleteCartItem(@Param("productId") Long productId, @Param("cartId") Long cartId);
}