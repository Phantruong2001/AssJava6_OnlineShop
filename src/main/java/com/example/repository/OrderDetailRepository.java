package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
	List<OrderDetail> findByOrderId(String id);
}