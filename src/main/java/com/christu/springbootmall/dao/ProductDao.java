package com.christu.springbootmall.dao;

import com.christu.springbootmall.constant.ProductCategory;
import com.christu.springbootmall.dto.ProductQueryParams;
import com.christu.springbootmall.dto.ProductRequest;
import com.christu.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProduct(Integer productId);
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Integer countProduct(ProductQueryParams productQueryParams);
}
