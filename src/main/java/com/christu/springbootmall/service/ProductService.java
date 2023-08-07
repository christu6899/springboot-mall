package com.christu.springbootmall.service;

import com.christu.springbootmall.dto.ProductRequest;
import com.christu.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
}
