package com.example.demo.intergrationTest;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.enums.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartRepository cartRepository;

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

		products.add(new Product(5, "Milk", ProductType.TOPPINGS, BigDecimal.valueOf(2)));
		products.add(new Product(6, "Hazelnut syrup", ProductType.TOPPINGS, BigDecimal.valueOf(3)));
		products.add(new Product(7, "Chocolate sauce", ProductType.TOPPINGS, BigDecimal.valueOf(5)));
		products.add(new Product(8, "Lemon", ProductType.TOPPINGS, BigDecimal.valueOf(2)));

		return products;
	}

	private List<CartItem> getItems() {
		List<CartItem> cartItems = new ArrayList<>();

		cartItems.add(new CartItem(1, getProducts().get(0), 3));
		cartItems.add(new CartItem(2, getProducts().get(1), 1));
		cartItems.add(new CartItem(3, getProducts().get(2), 0));
		cartItems.add(new CartItem(4, getProducts().get(3), 1));
		cartItems.add(new CartItem(5, getProducts().get(4), 2));
		cartItems.add(new CartItem(6, getProducts().get(5), 2));

		return cartItems;
	}

	@Test
	public void createCartForACustomer() throws Exception {
		when(cartRepository.save(cartsModel())).thenReturn(cartsModel());

		mockMvc.perform(post("/api/carts")
						.content("{json}")
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void createCartForTheSameCustomer_throwsUniqueConstraintException() throws Exception {
		when(cartRepository.save(cartsModel())).thenReturn(cartsModel());

		mockMvc.perform(post("/api/carts")
						.content("{json}")
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void updateCartForACustomer_increaseQuantity() throws Exception {
		when(cartRepository.save(cartsModel())).thenReturn(cartsModel());

		mockMvc.perform(put("/api/carts")
						.content("{json}")
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void deleteCartForACustomer_increaseQuantity() throws Exception {
		when(cartRepository.save(cartsModel())).thenReturn(cartsModel());

		mockMvc.perform(delete("/api/carts")
						.content("{json}")
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}
}