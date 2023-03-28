package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Favority;
import com.example.repository.FavorityRepository;
import com.example.service.FavorityService;

@Service
public class FavorityServiceImpl implements FavorityService {
	@Autowired
	FavorityRepository favorityRepository;

	@Override
	public Favority save(Favority entity) {
		Favority favority = favorityRepository.save(entity);
		return favority;

	}

	@Override
	public Favority findById(String id) {
		Favority favority = favorityRepository.findById(id).get();
		return favority;
	}

	@Override
	public void deleteById(String id) {
		favorityRepository.deleteById(id);
	}

	@Override
	public Favority findByUserIdAndProductId(String userId, String productId) {
		Favority favority = favorityRepository.findByUserIdAndProductId(userId, productId);
		return favority;
	}
}
