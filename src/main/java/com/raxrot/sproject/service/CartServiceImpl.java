package com.raxrot.sproject.service;

import com.raxrot.sproject.dto.CartDTO;
import com.raxrot.sproject.dto.ProductDTO;
import com.raxrot.sproject.exception.APIException;
import com.raxrot.sproject.model.Cart;
import com.raxrot.sproject.model.CartItem;
import com.raxrot.sproject.model.Product;
import com.raxrot.sproject.repository.CartItemRepository;
import com.raxrot.sproject.repository.CartRepository;
import com.raxrot.sproject.repository.ProductRepository;
import com.raxrot.sproject.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new APIException("no product found"));

        CartItem cartItem = cartItemRepository.findByProduct_IdAndCart_CartId(productId, cart.getCartId());

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0 || product.getQuantity() < quantity) {
            throw new APIException("Not enough quantity available for " + product.getProductName());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        return buildCartDTO(cart);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }

        return carts.stream()
                .map(this::buildCartDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findByUserEmailAndCartId(emailId, cartId)
                .orElseThrow(() -> new APIException("no cart found"));
        return buildCartDTO(cart);
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, int quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findByUserEmail(emailId).orElseThrow(() -> new APIException("no cart found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new APIException("no product found"));

        CartItem cartItem = cartItemRepository.findByProduct_IdAndCart_CartId(productId, cart.getCartId());

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        int newQty = cartItem.getQuantity() + quantity;
        if (newQty < 0) {
            throw new APIException("Quantity cannot be negative");
        }
        if (newQty == 0) {
            deleteProductFromCart(cart.getCartId(), productId);

            CartDTO updatedCartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> updatedProducts = cart.getCartItems().stream()
                    .collect(Collectors.toMap(
                            item -> item.getProduct().getId(),
                            item -> {
                                ProductDTO dto = modelMapper.map(item.getProduct(), ProductDTO.class);
                                dto.setQuantity(item.getQuantity());
                                return dto;
                            },
                            (a, b) -> a
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            updatedCartDTO.setProducts(updatedProducts);
            return updatedCartDTO;
        }

        cartItem.setQuantity(newQty);
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        return buildCartDTO(cart);
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new APIException("no cart found"));

        CartItem cartItem = cartItemRepository.findByProduct_IdAndCart_CartId(productId, cartId);

        if (cartItem == null) {
            throw new APIException("no product found");
        }

        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItem(productId, cartId);
        cartRepository.save(cart);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }

    private Cart createCart() {
        return cartRepository.findByUserEmail(authUtil.loggedInEmail()).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setTotalPrice(0.00);
            cart.setUser(authUtil.loggedInUser());
            return cartRepository.save(cart);
        });
    }

    private CartDTO buildCartDTO(Cart cart) {
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> products = cart.getCartItems().stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        item -> {
                            ProductDTO dto = modelMapper.map(item.getProduct(), ProductDTO.class);
                            dto.setQuantity(item.getQuantity());
                            return dto;
                        },
                        (a, b) -> a
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
        cartDTO.setProducts(products);
        return cartDTO;
    }
}
