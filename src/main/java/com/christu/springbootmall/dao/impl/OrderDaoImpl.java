package com.christu.springbootmall.dao.impl;

import com.christu.springbootmall.dao.OrderDao;
import com.christu.springbootmall.dto.OrderQueryParams;
import com.christu.springbootmall.model.Order;
import com.christu.springbootmall.model.OrderItem;
import com.christu.springbootmall.model.Product;
import com.christu.springbootmall.rowmapper.OrderItemRowMapper;
import com.christu.springbootmall.rowmapper.OrderRowMapper;
import com.christu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql ="INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) " +
                    "VALUES ( :userId, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("totalAmount",totalAmount);
        Date now =new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);

        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        String sql ="INSERT INTO order_item (order_id, product_id, quantity, amount) " +
                    "VALUES (:orderId, :productId, :quantity, :amount)";

        MapSqlParameterSource[] parameterSources =new MapSqlParameterSource[orderItemList.size()];
        for(int i=0;i<orderItemList.size();i++){
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderId",orderId);
            parameterSources[i].addValue("productId",orderItem.getProductId());
            parameterSources[i].addValue("quantity",orderItem.getQuantity());
            parameterSources[i].addValue("amount",orderItem.getAmount());
        }

        jdbcTemplate.batchUpdate(sql,parameterSources);
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql ="SELECT order_id,user_id,total_amount,created_date,last_modified_date " +
                    "FROM `order` WHERE order_id = :orderId";
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderId);

        List<Order> orderList = jdbcTemplate.query(sql,map,new OrderRowMapper());

        if(orderList.size()>0){
            return orderList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql ="SELECT oi.order_item_id,oi.order_id,oi.product_id,oi.quantity,oi.amount," +
                    "p.product_name,p.image_url " +
                    "FROM order_item oi LEFT JOIN product p ON oi.product_id=p.product_id "+
                    "WHERE oi.order_id = :orderId";

        Map<String,Object> map =new HashMap<>();
        map.put("orderId",orderId);

        List<OrderItem> orderItemList = jdbcTemplate.query(sql,map,new OrderItemRowMapper());

        return orderItemList;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT count(*) FROM `order` WHERE 1=1";

        Map<String, Object> map = new HashMap<>();
        sql = addFilterSql(sql,map,orderQueryParams);

        Integer total = jdbcTemplate.queryForObject(sql,map,Integer.class);
        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql ="SELECT order_id,user_id,total_amount,created_date, last_modified_date" +
                    " FROM `order` WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        sql = addFilterSql(sql,map,orderQueryParams);
        map.put("limit",orderQueryParams.getLimit());
        map.put("offset",orderQueryParams.getOffset());
        sql +=" Order BY created_date DESC LIMIT :limit OFFSET :offset";
        List<Order> orderList = jdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;
    }
    private String addFilterSql(String sql,Map<String,Object> map,OrderQueryParams orderQueryParams){
        if(orderQueryParams.getUserId()!=null){
            sql += " AND user_id = :userId";
            map.put("userId",orderQueryParams.getUserId());
        }
        return sql;
    }
}
