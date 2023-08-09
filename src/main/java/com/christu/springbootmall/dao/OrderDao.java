package com.christu.springbootmall.dao;

import com.christu.springbootmall.dto.CreateOrderRequest;
import com.christu.springbootmall.dto.OrderQueryParams;
import com.christu.springbootmall.model.Order;
import com.christu.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId,Integer totalAmount);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
    Order getOrderById(Integer orderId);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);
}
