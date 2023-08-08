package com.christu.springbootmall.dao.impl;

import com.christu.springbootmall.dao.UserDao;
import com.christu.springbootmall.dto.UserRegisterRequest;
import com.christu.springbootmall.model.Product;
import com.christu.springbootmall.model.User;
import com.christu.springbootmall.rowmapper.ProductRowMapper;
import com.christu.springbootmall.rowmapper.UserRowMapper;
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
public class UserDaoImpl implements UserDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql ="INSERT INTO user (email, password,created_date, last_modified_date) " +
                    "VALUES (:email,:password,:createDate,:lastModifiedDate)";

        Map<String,Object> map = new HashMap<>();
        map.put("email",userRegisterRequest.getEmail());
        map.put("password",userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);

        int userId = keyHolder.getKey().intValue();
        System.out.println(userId);
        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql ="SELECT user_id,email,password,created_date,last_modified_date " +
                    "FROM user WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        List<User> userList = jdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size()>0){
            return userList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql ="SELECT user_id,email,password,created_date,last_modified_date " +
                "FROM user WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email",email);
        List<User> userList = jdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size()>0){
            return userList.get(0);
        }else{
            return null;
        }
    }
}
