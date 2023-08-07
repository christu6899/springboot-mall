package com.christu.springbootmall.service;

import com.christu.springbootmall.constant.ProductCategory;
import com.christu.springbootmall.dto.ProductRequest;
import com.christu.springbootmall.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId,ProductRequest productRequest);
    void deleteProduct(Integer productId);
    List<Product> getProducts(ProductCategory category,String search);
}
