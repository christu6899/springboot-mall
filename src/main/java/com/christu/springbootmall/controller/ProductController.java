package com.christu.springbootmall.controller;

import com.christu.springbootmall.constant.ProductCategory;
import com.christu.springbootmall.dto.ProductQueryParams;
import com.christu.springbootmall.dto.ProductRequest;
import com.christu.springbootmall.model.Product;
import com.christu.springbootmall.service.ProductService;
import com.christu.springbootmall.util.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api()
@Validated
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation("Get Product by id")
    @ApiResponses({
            @ApiResponse(code=200,message="成功"),
            @ApiResponse(code=400,message="找不到該產品")
    })
    @GetMapping(value = "/products/{productId}",produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);
        if(product!=null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation("Create Product")
    @ApiResponses({
            @ApiResponse(code=201,message="新增成功"),
            @ApiResponse(code=400,message="新增失敗")
    })
    @PostMapping(value = "/products",produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @ApiOperation("Update Product")
    @ApiResponses({
            @ApiResponse(code=200,message="更新成功"),
            @ApiResponse(code=404,message="沒有該產品")
    })
    @PutMapping(value = "/products/{productId}",produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,@RequestBody @Valid ProductRequest productRequest){
        //check product already exist
        Product Product = productService.getProductById(productId);
        if(Product==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //update product
        productService.updateProduct(productId,productRequest);
        Product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }
    @ApiOperation("Delete Product")
    @ApiResponses({
            @ApiResponse(code=204,message="已被刪除"),
    })
    @DeleteMapping(value = "/products/{productId}",produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @ApiOperation("Get Products")
    @ApiResponses({
            @ApiResponse(code=200,message="取得成功"),
            @ApiResponse(code=404,message="沒有該產品")
    })
    @GetMapping(value = "/products",produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<Page<Product>> getProducts(
            //query condition
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            //sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,
            //select page
            @RequestParam(defaultValue = "5")@Max(1000)@Min(0) Integer limit,
            @RequestParam(defaultValue = "0")@Min(0) Integer offset
            ){

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        List<Product> productList = productService.getProducts(productQueryParams);
        Integer total = productService.countProduct(productQueryParams);

        Page<Product> page= new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

}
