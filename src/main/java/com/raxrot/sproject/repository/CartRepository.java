package com.raxrot.sproject.repository;

import com.raxrot.sproject.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email AND c.cartId = :cartId")
    Optional<Cart> findByUserEmailAndCartId(
            @Param("email") String email,
            @Param("cartId") Long cartId
    );

}
