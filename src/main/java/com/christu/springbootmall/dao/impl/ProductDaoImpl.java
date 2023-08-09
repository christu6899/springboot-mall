package com.christu.springbootmall.dao.impl;

import com.christu.springbootmall.dao.ProductDao;
import com.christu.springbootmall.dto.ProductQueryParams;
import com.christu.springbootmall.dto.ProductRequest;
import com.christu.springbootmall.model.Product;
import com.christu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public Product getProductById(Integer productId) {
        String sql ="SELECT product_id,product_name, category, image_url, price, stock, description," +
                    "created_date, last_modified_date " +
                    "FROM product WHERE product_id=:productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId",productId);
        List<Product> productList = jdbcTemplate.query(sql, map, new ProductRowMapper());

        if(productList.size()>0){
            return productList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql ="INSERT INTO product (product_name, category, image_url, price, stock," +
                    "description, created_date, last_modified_date) " +
                    "VALUES (:productName,:category,:imageUrl,:price,:stock," +
                    ":description,:createDate,:lastModifiedDate)";

        Map<String,Object> map = new HashMap<>();
        map.put("productName",productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        Date now = new Date();
        map.put("createDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);

        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql ="UPDATE product SET product_name = :productName,category = :category,image_url = :imageUrl," +
                    "price = :price,stock = :stock,description = :description,last_modified_date = :lastModifiedDate" +
                    " WHERE product_id=:productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);
        map.put("productName",productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        map.put("lastModifiedDate",new Date());

        jdbcTemplate.update(sql,map);
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        String sql ="UPDATE product SET stock = :stock,last_modified_date = :lastModifiedDate" +
                " WHERE product_id=:productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);
        map.put("stock",stock);
        map.put("lastModifiedDate",new Date());

        jdbcTemplate.update(sql,map);
    }

    @Override
    public void deleteProduct(Integer productId) {
        String sql = "DELETE FROM product where product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);

        jdbcTemplate.update(sql,map);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql ="SELECT product_id,product_name, category, image_url, price," +
                    "stock, description, created_date, last_modified_date FROM product WHERE 1=1";
        Map<String, Object> map = new HashMap<>();

        //search condition
        sql = addFilteringSql(sql,map,productQueryParams);

        //sorting
        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        //pagenation
        sql+= " LIMIT :limit OFFSET :offset";

        map.put("limit",productQueryParams.getLimit());
        map.put("offset",productQueryParams.getOffset());
        List<Product> productList = jdbcTemplate.query(sql, map, new ProductRowMapper());

        return productList;
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(sql,map,productQueryParams);

        Integer total = jdbcTemplate.queryForObject(sql,map,Integer.class);
        return total;
    }

    private String addFilteringSql(String sql, Map<String,Object> map, ProductQueryParams productQueryParams){
        if(productQueryParams.getCategory()!=null){
            sql += " AND category = :category";
            map.put("category",productQueryParams.getCategory().name());
        }
        if(productQueryParams.getSearch()!=null){
            sql += " AND product_name LIKE :search";
            map.put("search","%" + productQueryParams.getSearch() + "%");
        }
        return sql;
    }


}
