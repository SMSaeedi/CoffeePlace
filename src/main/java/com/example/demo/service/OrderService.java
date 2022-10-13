package com.example.demo.service;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Order;
import com.example.demo.dao.entity.OrderItem;
import com.example.demo.dao.repository.CartItemRepository;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dao.repository.OrderItemRepository;
import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.dto.OrderDiscount;
import com.example.demo.dto.OrderDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.NotFoundException;
import com.example.demo.log.LogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.utils.OrderDiscountCalculation.orderDiscountCal;

@Service
@Transactional(rollbackFor = {SQLException.class})
public class OrderService {
    @Value("${service.order.orderDesc}")
    private String cancelOrderDesc;

    @Value("${service.order.exception.orderNotFound}")
    private String orderNotFound;

    @Value("${service.order.item.exception.orderItemNotFound}")
    private String orderItemNotFound;

    @Value("${service.cart.exception.cartNotFound}")
    String cartNotFound;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ObjectMapper mapper;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.mapper = mapper;
    }

    public OrderDto createOrder(int customerId) {
        LogInfo.logger.info("createOrder " + customerId);
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(cartNotFound));
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        Set<OrderItem> orderItems = cartItems.stream().map(this::saveCartItemToOrderItem).collect(Collectors.toSet());

        OrderDiscount bonusCalculate = orderDiscountCal(cart);

        Order newOrder = Order.builder()
                .orderItems(orderItems)
                .totalAmount(bonusCalculate.getTotalAmount())
                .description(bonusCalculate.getDescription())
                .customerId(customerId)
                .orderDate(new Date())
                .orderStatus(OrderStatus.REGISTERED)
                .build();

        Order order = orderRepository.save(newOrder);

        return toOrderDto(order);
    }

    private OrderItem saveCartItemToOrderItem(CartItem cartItem) {
        return orderItemRepository.save(OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .amount(cartItem.getProduct().getPrice())
                .build());
    }

    public OrderDto getOrderByCustomerId(int customerId) {
        LogInfo.logger.info("getOrderByCustomerId ");
        return toOrderDto(findOrderByCustomerId(customerId));
    }

    private Order findOrderByCustomerId(int customerId) {
        LogInfo.logger.info("findOrderByCustomerId ");
        return orderRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(orderNotFound));
    }

    public void cancelOrder(int orderId) {
        LogInfo.logger.info("cancelOrderItem ");
        Optional<Order> orderItemById = orderRepository.findById(orderId);

        if (orderItemById.isPresent()) {
            Order canceledOrder = Order.builder()
                    .id(orderId)
                    .orderStatus(OrderStatus.CANCELED)
                    .orderDate(new Date())
                    .description(cancelOrderDesc)
                    .build();
            orderRepository.save(canceledOrder);
        }
        throw new NotFoundException(orderItemNotFound);
    }

    private OrderDto toOrderDto(Order order) {
        return mapper.convertValue(order, OrderDto.class);
    }
}