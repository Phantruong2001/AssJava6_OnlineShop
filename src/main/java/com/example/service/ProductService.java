package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.model.Product;

public interface ProductService {

	Product saveOrUpdate(Product entity);

	Product findById(String id);

	List<Product> findByCategoryId(String id);

	void deleteById(String id);

	Page<Product> findAllByNameLike(String keyword, Pageable pageable);
	
	Page<Product> findAllByCategoryId(String id, Pageable pageable);
	
	Page<Product> findAll(Pageable pageable);
	
	List<Product> findByActive(boolean active);
	
	List<Product> findFavorityByUserId(String id);
	
	List<Product> findByFavorityTop10();
	
	List<Product> findByShareTop10();
	
	List<Product> findFavorityByUserIdAndCateId(String id, String cateId);

}
