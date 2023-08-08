package com.christu.springbootmall.service;

import com.christu.springbootmall.dto.CreateOrderRequest;

public interface OrderService {
    Integer createOrder(Integer userId,CreateOrderRequest createOrderRequest);
}
