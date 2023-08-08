package com.christu.springbootmall.dao;

import com.christu.springbootmall.dto.UserRegisterRequest;
import com.christu.springbootmall.model.User;

public interface UserDao {
    Integer createUser (UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);

    User getUserByEmail(String email);
}
