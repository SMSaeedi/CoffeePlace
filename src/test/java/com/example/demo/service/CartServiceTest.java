package com.example.demo.service;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.CartItemRepository;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dto.CartDto;
import com.example.demo.enums.ProductType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Mock
    private ProductService productService;
    @Mock
    CartRepository cartRepository;

    @Mock
    CartItemRepository cartItemRepository;

    private int sequenceId() {
        List<Integer> ret = IntStream.rangeClosed(1, 50).boxed().collect(Collectors.toList());

        int total = 0;
        for (int i : ret) {
            total += i;
            return total;
        }
        return total;
    }

    private Cart cartsModel() {
        return Cart.builder()
                .id(sequenceId())
                .items(getItems())
                .customerId(1)
                .build();
    }

    private List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Black Coffee", ProductType.COFFEE, BigDecimal.valueOf(4)));
        products.add(new Product(2, "Latte", ProductType.COFFEE, BigDecimal.valueOf(5)));
        products.add(new Product(3, "Mocha", ProductType.COFFEE, BigDecimal.valueOf(6)));
        products.add(new Product(4, "Tea", ProductType.COFFEE, BigDecimal.valueOf(3)));
        return products;
    }

    private List<CartItem> getItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1, getProducts().get(0), 1));
        cartItems.add(new CartItem(2, getProducts().get(1), 2));
        return cartItems;
    }

    @Test
    public void cartEntityWithZeroSize() {
        when(cartRepository.count()).thenReturn(0L);

        long userCount = cartRepository.count();

        assertEquals(0L, userCount);
        verify(cartRepository).count();
    }

    @Test
    public void createCartForACustomerId() {
        when(cartRepository.save(cartsModel())).thenReturn(cartsModel());
        when(cartItemRepository.save(getItems().get(0))).thenReturn(getItems().get(0));
        when(productService.getProductById(getProducts().get(0).getId())).thenReturn(getProducts().get(0));

        CartDto cartDto = cartService.addCart(cartsModel().getCustomerId(), cartsModel().getItems().get(0).getProduct().getId());

        assertEquals(cartsModel().getItems().size(), 2);
        assertEquals(cartsModel().getItems().get(0).getProduct().getName(), cartDto.getItems().get(0).getProduct().getName());

//        CartDto cartByToken = cartService.getCartByToken(cartsModel().getCustomerId());
//        verify(cartRepository).findByCustomerId(cartsModel().getCustomerId());
    }

    @Test
    public void createCartForTheSameCustomerId_throwsUniqueConstraintException() {
//        lenient().when(cartRepository.save(cartsModel())).thenReturn(cartsModel());
//        when(m.isOk()).thenThrow(new NotFoundException("cart already added for this customer"));
//        doThrow(exception).when(mock).someVoidMethod();
    }

    @Test
    public void updateCartForACustomerId_increaseQuantity() {
        when(cartRepository.findByCustomerId(1)).thenReturn(Optional.of(cartsModel()));

        CartDto cartDto = cartService.updateCart(cartsModel().getCustomerId(), cartsModel().getItems().get(0).getProduct().getId(), 5);

        assertEquals(cartsModel().getItems().size(), 2);
        assertEquals(cartsModel().getItems().get(0).getQuantity(), cartDto.getItems().get(0).getQuantity());
        assertEquals(cartsModel().getItems().get(0).getProduct().getName(), cartDto.getItems().get(0).getProduct().getName());

    }

    @Test
    public void updateCartForACustomerId_decreaseQuantityToZero() {
    }

    @Test
    public void updateCartForACustomerId_increaseDrinks_toGetToppingBones() {
    }

    @Test
    public void updateCartForACustomerId_addProductUpTo12Euro_toGetTheDiscount() {
    }

    @Test
    public void updateCartForACustomerId_calculateOnlyOneBones() {
    }
}