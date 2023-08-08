package com.christu.springbootmall.service;

import com.christu.springbootmall.dto.CreateOrderRequest;
import com.christu.springbootmall.model.Order;

public interface OrderService {
    Integer createOrder(Integer userId,CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);
}
