package com.example.demo.entityTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.InitBinder;

import javax.persistence.EntityManager;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartEntityTest {

    @Mock
    private EntityManager entityManager;

    @InitBinder
    void init() {
    }

    private final Long id;

    public CartEntityTest(Long id) {
        this.id = id;
    }
}