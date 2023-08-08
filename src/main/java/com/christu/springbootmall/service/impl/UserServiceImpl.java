package com.christu.springbootmall.service.impl;

import com.christu.springbootmall.dao.UserDao;
import com.christu.springbootmall.dto.UserLoginRequest;
import com.christu.springbootmall.dto.UserRegisterRequest;
import com.christu.springbootmall.model.User;
import com.christu.springbootmall.service.UserServiec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;


@Component
public class UserServiceImpl implements UserServiec {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if(user!=null){
            log.warn("該 email {} 已經被註冊",userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if(user==null){
            log.warn("該 email {} 不存在",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        if(user.getPassword().equals(hashedPassword)){
            return user;
        }else{
            log.warn("該 email {} 與密碼不符",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
