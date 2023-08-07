package com.christu.springbootmall.dao;

import com.christu.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}
