package com.christu.springbootmall.service;

import com.christu.springbootmall.dto.CreateOrderRequest;
import com.christu.springbootmall.dto.OrderQueryParams;
import com.christu.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    Integer createOrder(Integer userId,CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);
}
