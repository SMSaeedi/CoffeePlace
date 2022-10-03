package com.example.demo.service;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Order;
import com.example.demo.dao.entity.OrderItem;
import com.example.demo.dao.repository.CartItemRepository;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dao.repository.OrderItemRepository;
import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderItemDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.log.LogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Value("${service.order.descTopping}")
    private String descTopping;

    @Value("${service.order.orderDesc}")
    private String desc;

    @Value("${service.order.descDiscount}")
    private String descDiscount;

    @Value("${service.order.none}")
    private String noBones;

    @Value("${service.order.exception.orderNotFound}")
    private String orderNotFound;

    @Value("${service.order.item.orderItemNotFound}")
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
        LogInfo.logger.info("createOrder ");
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(cartNotFound));
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());

        List<OrderItem> orderItems = cartItems.stream().map(this::cartItemToOrderItem).collect(Collectors.toList());
        Order newOrder = calculateBones(toOrderDto(Order.builder()
                .orderItems(orderItems)
                .build()));
        newOrder.setOrderDate(new Date());
        newOrder.setCustomerId(customerId);
        newOrder.setOrderStatus(OrderStatus.REGISTERED);

        return toOrderDto(orderRepository.save(newOrder));
    }

    private OrderItem cartItemToOrderItem(CartItem cartItem) {
        return orderItemRepository.save(OrderItem.builder()
                .cartItemId(cartItem.getId())
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
                    .description(desc)
                    .build();
            orderRepository.save(canceledOrder);
        }
        throw new NotFoundException(orderItemNotFound);
    }

    private Order calculateBones(OrderDto orderDto) {
        boolean freeTopping = false;
        BigDecimal oneFreeTopping = BigDecimal.ZERO;
        boolean discount = false;
        BigDecimal discount_25Percent = BigDecimal.ZERO;

        List<OrderItemDto> toppingsList = orderDto.getOrderItems().stream().filter(orderDetails -> orderDetails.getProduct().getType().equals(ProductType.TOPPINGS)).collect(Collectors.toList());
        Optional<OrderItemDto> minPriceToppingItem = toppingsList.stream().min(Comparator.comparing(cartItemDto -> cartItemDto.getProduct().getPrice()));

        boolean drinksItems = orderDto.getOrderItems().stream().filter(cartItemDto -> cartItemDto.getProduct().getType().equals(ProductType.COFFEE)).collect(Collectors.toList()).size() >= 3;
        if (drinksItems) {
            freeTopping = true;
            oneFreeTopping = orderDto.getTotalAmount().subtract(minPriceToppingItem.get().getProduct().getPrice());
        }

        if (orderDto.getTotalAmount().compareTo(BigDecimal.valueOf(12)) >= 0) {
            discount = true;
            discount_25Percent = orderDto.getTotalAmount().subtract(orderDto.getTotalAmount().multiply(BigDecimal.valueOf(25)).divide(BigDecimal.valueOf(100)));
        }

        if (freeTopping && discount)
            if (discount_25Percent.compareTo(oneFreeTopping) >= 0) {
                orderDto.setTotalAmount(oneFreeTopping);
                orderDto.setDescription(descTopping);
            } else if (oneFreeTopping.compareTo(discount_25Percent) >= 0) {
                orderDto.setTotalAmount(discount_25Percent);
                orderDto.setDescription(descDiscount);
            } else
                orderDto.setDescription(noBones);

        return toOrderEntity(orderDto);
    }

    private OrderDto toOrderDto(Order order) {
        return mapper.convertValue(order, OrderDto.class);
    }

    private Order toOrderEntity(OrderDto orderDto) {
        return mapper.convertValue(orderDto, Order.class);
    }
}