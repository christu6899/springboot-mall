package com.christu.springbootmall.service.impl;

import com.christu.springbootmall.dao.OrderDao;
import com.christu.springbootmall.dao.ProductDao;
import com.christu.springbootmall.dto.BuyItem;
import com.christu.springbootmall.dto.CreateOrderRequest;
import com.christu.springbootmall.model.OrderItem;
import com.christu.springbootmall.model.Product;
import com.christu.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Override
    @Transactional
    public Integer createOrder(Integer userId,CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){

            //calculate total amount
            Product product = productDao.getProductById(buyItem.getProductId());
            int amount= product.getPrice() * buyItem.getQuantity();
            totalAmount +=amount;

            //BuyItem to OrderItem
            OrderItem orderItem =new OrderItem();
            orderItem.setProductId(product.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId,totalAmount);
        orderDao.createOrderItems(orderId,orderItemList);
        return  orderId;
    }
}
