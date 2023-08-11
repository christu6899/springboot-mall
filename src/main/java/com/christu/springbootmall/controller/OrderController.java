package com.christu.springbootmall.controller;

import com.christu.springbootmall.dto.CreateOrderRequest;
import com.christu.springbootmall.dto.OrderQueryParams;
import com.christu.springbootmall.model.Order;
import com.christu.springbootmall.service.OrderService;
import com.christu.springbootmall.util.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
@Api()
@Validated
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("Create Order")
    @ApiResponses({
            @ApiResponse(code=201,message="創建成功"),
            @ApiResponse(code=400,message="無該使用者或商品、庫存不足")
    })
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
        Integer orderId =  orderService.createOrder(userId,createOrderRequest);
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @ApiOperation("Get Orders")
    @ApiResponses({
            @ApiResponse(code=200,message="取得成功"),
    })
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(@PathVariable Integer userId,
                                           @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer offset){
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        List<Order> orderList = orderService.getOrders(orderQueryParams);

        Integer count = orderService.countOrder(orderQueryParams);
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
