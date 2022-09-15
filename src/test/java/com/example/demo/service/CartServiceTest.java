//package com.example.demo.service;
//
//import com.example.demo.dao.entity.Cart;
//import com.example.demo.dao.entity.CartItem;
//import com.example.demo.dao.entity.Product;
//import com.example.demo.dao.repository.CartItemRepository;
//import com.example.demo.dao.repository.CartRepository;
//import com.example.demo.dto.CartDto;
//import com.example.demo.enums.ProductType;
//import com.example.demo.service.impl.CartServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class CartServiceTest {
//
//    @Autowired
//    private CartServiceImpl cartService;
//
//    @MockBean
//    private ProductService productService;
//
//    @MockBean
//    CartRepository cartRepository;
//
//    @MockBean
//    CartItemRepository cartItemRepository;
//
//    private int sequenceId() {
//        List<Integer> ret = IntStream.rangeClosed(1, 50).boxed().collect(Collectors.toList());
//
//        int total = 0;
//        for (int i : ret) {
//            total += i;
//            return total;
//        }
//        return total;
//    }
//
//    private Cart cartsModel() {
//        return Cart.builder()
//                .id(sequenceId())
//                .items(getItems())
//                .customerId(1)
//                .build();
//    }
//
//    private List<Product> getProducts() {
//        List<Product> products = new ArrayList<>();
//
//        products.add(new Product(1, "Black Coffee", ProductType.COFFEE, BigDecimal.valueOf(4)));
//        products.add(new Product(2, "Latte", ProductType.COFFEE, BigDecimal.valueOf(5)));
//        products.add(new Product(3, "Mocha", ProductType.COFFEE, BigDecimal.valueOf(6)));
//        products.add(new Product(4, "Tea", ProductType.COFFEE, BigDecimal.valueOf(3)));
//
//        products.add(new Product(5, "Milk", ProductType.TOPPINGS, BigDecimal.valueOf(2)));
//        products.add(new Product(6, "Hazelnut syrup", ProductType.TOPPINGS, BigDecimal.valueOf(3)));
//        products.add(new Product(7, "Chocolate sauce", ProductType.TOPPINGS, BigDecimal.valueOf(5)));
//        products.add(new Product(8, "Lemon", ProductType.TOPPINGS, BigDecimal.valueOf(2)));
//
//        return products;
//    }
//
//    private List<CartItem> getItems() {
//        List<CartItem> cartItems = new ArrayList<>();
//
//        cartItems.add(new CartItem(1, getProducts().get(0), 3));
//        cartItems.add(new CartItem(2, getProducts().get(1), 1));
//        cartItems.add(new CartItem(3, getProducts().get(2), 0));
//        cartItems.add(new CartItem(4, getProducts().get(3), 1));
//        cartItems.add(new CartItem(5, getProducts().get(4), 2));
//        cartItems.add(new CartItem(6, getProducts().get(5), 2));
//
//        return cartItems;
//    }
//
//    @Test
//    public void cartEntityWithZeroSize() {
//        when(cartRepository.count()).thenReturn(0L);
//
//        assertEquals(0L, cartRepository.count());
//        verify(cartRepository).count();
//    }
//
//    @Test
//    public void cartEntityWithSize() {
//        when(cartRepository.save(cartsModel())).thenReturn(cartsModel());
//        when(cartRepository.count()).thenReturn(7L);
//
//        assertEquals(7L, cartRepository.count());
//        verify(cartRepository).count();
//    }
//
//    @Test
//    public void createCartForACustomer() {
//        when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cartsModel());
//        when(cartItemRepository.save(getItems().get(0))).thenReturn(getItems().get(0));
//        when(productService.getProductById(getProducts().get(0).getId())).thenReturn(getProducts().get(0));
//
//        CartDto cartDto = cartService.addCart(cartsModel().getCustomerId(), cartsModel().getItems().get(0).getProduct().getId());
//
//        assertEquals(cartsModel().getItems().size(), 6);
//        assertEquals(cartsModel().getItems().get(0).getProduct().getName(), cartDto.getItems().get(0).getProduct().getName());
//    }
//
//    @Test
//    public void updateCartForACustomer_increaseQuantity() {
//        when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cartsModel());
//        when(cartRepository.findByCustomerId(cartsModel().getCustomerId())).thenReturn(Optional.of(cartsModel()));
//
//        CartDto cartDto = cartService.updateCart(cartsModel().getCustomerId(), cartsModel().getItems().get(1).getProduct().getId(), 5);
//
//        assertEquals(cartsModel().getItems().size(), cartDto.getItems().size());
//        assertEquals(cartsModel().getItems().get(1).getQuantity(), cartDto.getItems().get(1).getQuantity());
//        assertEquals(cartsModel().getItems().get(1).getProduct().getName(), cartDto.getItems().get(1).getProduct().getName());
//    }
//
//    @Test
//    public void updateCartForACustomer_zeroQuantity_toRemoveItem() {
//        when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cartsModel());
//        when(cartRepository.findByCustomerId(cartsModel().getCustomerId())).thenReturn(Optional.of(cartsModel()));
//
//        CartDto cartDto = cartService.updateCart(cartsModel().getCustomerId(), cartsModel().getItems().get(2).getProduct().getId(), 0);
//
//        assertEquals(cartsModel().getItems().size(), cartDto.getItems().size());
//        assertEquals(cartsModel().getItems().get(2).getQuantity(), cartDto.getItems().get(2).getQuantity());
//        assertEquals(cartsModel().getItems().get(2).getProduct().getName(), cartDto.getItems().get(2).getProduct().getName());
//    }
//}