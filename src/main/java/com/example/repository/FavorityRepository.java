package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Favority;

@Repository
public interface FavorityRepository extends JpaRepository<Favority, String> {
	Favority findByUserIdAndProductId(String userId, String productId);
}