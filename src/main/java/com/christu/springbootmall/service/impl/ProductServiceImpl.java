package com.christu.springbootmall.service.impl;

import com.christu.springbootmall.dao.ProductDao;
import com.christu.springbootmall.dto.ProductQueryParams;
import com.christu.springbootmall.dto.ProductRequest;
import com.christu.springbootmall.model.Product;
import com.christu.springbootmall.service.ProductService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        Integer productId = productDao.createProduct(productRequest);
        if(productId==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else {
            return productId;
        }
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId,productRequest);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteProduct(productId);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }
}
