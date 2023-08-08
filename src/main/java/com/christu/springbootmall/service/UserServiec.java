package com.christu.springbootmall.service;

import com.christu.springbootmall.dto.UserLoginRequest;
import com.christu.springbootmall.dto.UserRegisterRequest;
import com.christu.springbootmall.model.User;

public interface UserServiec {
    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User login(UserLoginRequest userLoginRequest);
}
