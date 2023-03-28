package com.example.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.helper.StringHelper;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public Category saveOrUpdate(Category entity) {
		logger.info("saveOrUpdate");
		if (StringHelper.equals(entity.getId(), "") || !StringHelper.notNull(entity.getId())) {
			entity.setCreateDay(new Date());
			entity.setUpdateDate(new Date());
		} else {
			entity.setCreateDay(findById(entity.getId()).getCreateDay());
			entity.setUpdateDate(new Date());
		}
		Category category = categoryRepository.save(entity);
		return category;
	}

	@Override
	public Category findById(String id) {
		logger.info("findById");
		if (!StringHelper.notNull(id))
			return null;
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isEmpty())
			return null;
		return category.get();
	}

	@Override
	public void deleteById(String id) {
		logger.info("deleteById");
		categoryRepository.deleteById(id);
	}

	@Override
	public Page<Category> findAllByNameLike(String namekeyword, Pageable pageable) {
		logger.info("findAllByNameLike");
		Page<Category> page = categoryRepository.findAllByNameLike(namekeyword, pageable);
		return page;
	}

	@Override
	public List<Category> findAll() {
		logger.info("findAll");
		return categoryRepository.findAll();
	}

}
