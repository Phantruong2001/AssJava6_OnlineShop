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
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductRepository productRepository;

	@Override
	public Product saveOrUpdate(Product entity) {
		logger.info("saveOrUpdate");
		if (StringHelper.equals(entity.getId(), "") || !StringHelper.notNull(entity.getId())) {
			entity.setCreateDay(new Date());
			entity.setUpdateDate(new Date());
		} else {
			entity.setCreateDay(entity.getCreateDay());
			entity.setUpdateDate(new Date());
		}
		Product product = productRepository.save(entity);
		return product;
	}

	@Override
	public Product findById(String id) {
		logger.info("findById");
		if (!StringHelper.notNull(id))
			return null;
		Optional<Product> product = productRepository.findById(id);
		if (product.isEmpty())
			return null;
		return product.get();
	}

	@Override
	public List<Product> findByCategoryId(String id) {
		logger.info("findByCategoryId");
		if (!StringHelper.notNull(id))
			return null;
		List<Product> list = productRepository.findByCategoryId(id);
		return list;
	}

	@Override
	public void deleteById(String id) {
		logger.info("deleteById");
		productRepository.deleteById(id);
	}

	@Override
	public Page<Product> findAllByNameLike(String keyword, Pageable pageable) {
		logger.info("findAllByNameLike");
		Page<Product> page = productRepository.findAllByNameLike(keyword, pageable);
		return page;
	}

	@Override
	public Page<Product> findAllByCategoryId(String id, Pageable pageable) {
		logger.info("findAllByCategoryId");
		Page<Product> page = productRepository.findAllByCategoryId(id, pageable);
		return page;
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		logger.info("findAll");
		Page<Product> page = productRepository.findAll(pageable);
		return page;
	}

	@Override
	public List<Product> findByActive(boolean active) {
		logger.info("findByActive");
		List<Product> list = productRepository.findByActiveOrderByCategoryIdDesc(active);
		return list;
	}

	@Override
	public List<Product> findFavorityByUserId(String id) {
		logger.info("findFavorityByUserId");
		List<Product> list = productRepository.findByFavorityWithUserId(id);
		return list;
	}

	@Override
	public List<Product> findByFavorityTop10() {
		logger.info("findByFavorityTop10");
		List<Product> list = productRepository.findByFavorityTop10();
		return list;
	}

	@Override
	public List<Product> findByShareTop10() {
		logger.info("findByShareTop10");
		List<Product> list = productRepository.findByShareTop10();
		return list;
	}

	@Override
	public List<Product> findFavorityByUserIdAndCateId(String id, String cateId) {
		logger.info("findByFavorityWithUserIdAndCateId");
		List<Product> list = productRepository.findByFavorityWithUserIdAndCateId(id, cateId);
		return list;
	}

}
