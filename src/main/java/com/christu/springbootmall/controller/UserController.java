package com.christu.springbootmall.controller;

import com.christu.springbootmall.dto.UserLoginRequest;
import com.christu.springbootmall.dto.UserRegisterRequest;
import com.christu.springbootmall.model.User;
import com.christu.springbootmall.service.UserServiec;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Api()
@RestController
public class UserController {
    @Autowired
    private UserServiec userServiec;

    @ApiOperation("User Register")
    @ApiResponses({
            @ApiResponse(code=201,message="建立成功"),
            @ApiResponse(code=400,message="Email已被註冊")
    })
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        Integer userId = userServiec.register(userRegisterRequest);
        User user = userServiec.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @ApiOperation("User Login")
    @ApiResponses({
            @ApiResponse(code=200,message="登入成功"),
            @ApiResponse(code=400,message="Email尚未註冊或登入失敗")
    })
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody UserLoginRequest userLoginRequest){
        User user = userServiec.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
