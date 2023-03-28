package com.example.service;

import java.util.List;

import com.example.model.OrderDetail;

public interface OrderDetailService {

	OrderDetail saveOrUpdate(OrderDetail entity);

	OrderDetail findById(String id);

	List<OrderDetail> findByOrderId(String id);
}
