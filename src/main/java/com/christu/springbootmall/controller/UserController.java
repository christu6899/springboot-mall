package com.christu.springbootmall.controller;

import com.christu.springbootmall.dto.UserRegisterRequest;
import com.christu.springbootmall.model.User;
import com.christu.springbootmall.service.UserServiec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserServiec userServiec;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        Integer userId = userServiec.register(userRegisterRequest);
        User user = userServiec.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
