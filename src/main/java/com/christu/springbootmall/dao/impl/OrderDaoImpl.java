package com.christu.springbootmall.dao.impl;

import com.christu.springbootmall.dao.OrderDao;
import com.christu.springbootmall.model.OrderItem;
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
}
