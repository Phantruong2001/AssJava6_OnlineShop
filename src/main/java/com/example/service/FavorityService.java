package com.example.service;

import com.example.model.Favority;

public interface FavorityService {

	Favority save(Favority entity);

	Favority findById(String id);
	
	Favority findByUserIdAndProductId(String userId, String productId);

	void deleteById(String id);
}
