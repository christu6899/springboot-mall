package com.christu.springbootmall.service.impl;

import com.christu.springbootmall.dao.OrderDao;
import com.christu.springbootmall.dao.ProductDao;
import com.christu.springbootmall.dao.UserDao;
import com.christu.springbootmall.dto.BuyItem;
import com.christu.springbootmall.dto.CreateOrderRequest;
import com.christu.springbootmall.dto.OrderQueryParams;
import com.christu.springbootmall.model.Order;
import com.christu.springbootmall.model.OrderItem;
import com.christu.springbootmall.model.Product;
import com.christu.springbootmall.model.User;
import com.christu.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;
    @Override
    @Transactional
    public Integer createOrder(Integer userId,CreateOrderRequest createOrderRequest) {

        User user = userDao.getUserById(userId);
        if(user==null){
            log.warn("該 userId {} 不存在",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){

            //calculate total amount
            Product product = productDao.getProductById(buyItem.getProductId());
            if(product == null){
                log.warn("商品 {} 不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock()<buyItem.getQuantity()) {
                log.warn("商品 {} 庫存不足，無法購買，剩餘庫存 {}，欲購買數量{}",
                        buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDao.updateStock(product.getProductId(),product.getStock()- buyItem.getQuantity());

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

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);
        order.setOrderItemList(orderItemList);
        return order;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for(Order order : orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }
}
