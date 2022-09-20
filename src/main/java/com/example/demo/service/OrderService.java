package com.example.demo.service;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Order;
import com.example.demo.dao.entity.OrderItem;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dao.repository.OrderItemRepository;
import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderItemDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    @Value("${service.order.descTopping}")
    private String descTopping;

    @Value("${service.order.descDiscount}")
    private String descDiscount;

    @Value("${service.order.exception.orderNotFound}")
    private String orderNotFound;

    @Value("${service.order.item.orderItemNotFound}")
    private String orderItemNotFound;

    @Value("${service.cart.exception.cartNotFound}")
    String cartNotFound;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ObjectMapper mapper;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    public OrderDto createOrder(int token, OrderDto request) {
        log.debug("createOrder ", request);
        Cart cart = cartRepository.findByCustomerId(token).orElseThrow(() -> new NotFoundException(cartNotFound));

        List<OrderItem> orderItems = cart.getItems().stream().map(this::cartItemToOrderItem).collect(Collectors.toList());

        Order newOrder = orderRepository.save(Order.builder()
                .orderItems(orderItems)
                .customerId(token)
                .orderDate(new Date())
                .totalAmount(calculateBones(request))
                .orderStatus(OrderStatus.REGISTERED)
                .build());

        return mapper.convertValue(newOrder, OrderDto.class);
    }

    private OrderItem cartItemToOrderItem(CartItem cartItem) {
        return orderItemRepository.save(OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .amount(cartItem.getProduct().getPrice())
                .build());
    }

    public OrderDto getOrderByCustomerId(int customerId) {
        log.debug("getOrderByCustomerId ");
        return mapper.convertValue(findOrderByCustomerId(customerId), OrderDto.class);
    }

    private Order findOrderByCustomerId(int customerId) {
        log.debug("findOrderByCustomerId ");
        return orderRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(orderNotFound));
    }

    public void cancelOrderItem(int orderItemId) {
        log.debug("cancelOrderItem ");
        Optional<OrderItem> orderItemById = orderItemRepository.findById(orderItemId);

        if (orderItemById.isPresent())
            orderItemRepository.deleteById(orderItemId);
        orderItemById.orElseThrow(() -> new NotFoundException(orderItemNotFound));
    }


    private BigDecimal calculateBones(OrderDto orderDto) {
        boolean freeTopping = false;
        BigDecimal toppingFreeAmount = BigDecimal.ZERO;
        boolean discount = false;
        BigDecimal discountAmount = BigDecimal.ZERO;

        List<OrderItemDto> toppingsList = orderDto.getOrderDetails().stream().filter(orderDetails -> orderDetails.getProduct().getType().equals(ProductType.TOPPINGS)).collect(Collectors.toList());
        Optional<OrderItemDto> minPriceToppingItem = toppingsList.stream().min(Comparator.comparing(cartItemDto -> cartItemDto.getProduct().getPrice()));

        boolean drinksItems = orderDto.getOrderDetails().stream().filter(cartItemDto -> cartItemDto.getProduct().getType().equals(ProductType.COFFEE)).collect(Collectors.toList()).size() >= 3;
        if (drinksItems) {
            freeTopping = true;
            orderDto.setTotalAmount(orderDto.getTotalAmount().subtract(minPriceToppingItem.get().getProduct().getPrice()));
            toppingFreeAmount = orderDto.getTotalAmount();
            orderDto.setDescription(descTopping);
        }

        if (orderDto.getTotalAmount().compareTo(BigDecimal.valueOf(12)) >= 1) {
            discount = true;
            orderDto.getTotalAmount().subtract(orderDto.getTotalAmount().multiply(BigDecimal.valueOf(25)).divide(BigDecimal.valueOf(100)));
            discountAmount = orderDto.getTotalAmount();
            orderDto.setDescription(descDiscount);
        }

        if (freeTopping && discount)
            if (discountAmount.compareTo(toppingFreeAmount) >= 1) {
                orderDto.setTotalAmount(toppingFreeAmount);
                return orderDto.getTotalAmount();
            } else {
                orderDto.setTotalAmount(discountAmount);
                return orderDto.getTotalAmount();
            }
        return orderDto.getTotalAmount();
    }
}