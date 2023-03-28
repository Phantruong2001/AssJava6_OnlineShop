package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.model.Category;

public interface CategoryService {

	Category saveOrUpdate(Category entity);

	Category findById(String id);

	void deleteById(String id);

	Page<Category> findAllByNameLike(String namekeyword, Pageable pageable);

	List<Category> findAll();
}
