package com.example.demo.intergrationTest;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;

@SpringBootTest
@WebMvcTest
@AutoConfigureMockMvc
public class CartIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void whenCreateCartWithUserIdAndProductId_thenCorrectResponse() throws Exception {
		MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
		String user = "{\"name\": \"bob\", \"email\" : \"bob@domain.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/carts/add/1")
						.header(String.valueOf(1))
						.content(user)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentType(textPlainUtf8));
	}

	@Test
	public void whenUpdateCartWithUserIdAndProductIdAndQuantity_thenCorrectResponse() throws Exception {
		String user = "{\"name\": \"\", \"email\" : \"bob@domain.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/carts/update/1/5")
						.header(String.valueOf(1))
						.content(user)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is mandatory")))
				.andExpect(MockMvcResultMatchers.content()
						.contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	public void whenGetCartsByUserId_thenCorrectResponse() throws Exception {
		String user = "{\"name\": \"\", \"email\" : \"bob@domain.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/carts")
						.header(String.valueOf(1))
						.content(user)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is mandatory")))
				.andExpect(MockMvcResultMatchers.content()
						.contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	public void whenRemoveCartByCartId_thenCorrectResponse() throws Exception {
		String user = "{\"name\": \"\", \"email\" : \"bob@domain.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/carts/remove")
						.content(user)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is mandatory")))
				.andExpect(MockMvcResultMatchers.content()
						.contentType(MediaType.APPLICATION_JSON_UTF8));
	}

}