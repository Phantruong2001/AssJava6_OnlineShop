package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.OrderDetail;
import com.example.repository.OrderDetailRepository;
import com.example.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Override
	public OrderDetail saveOrUpdate(OrderDetail entity) {
		OrderDetail orderDetail = orderDetailRepository.save(entity);
		return orderDetail;
	}

	@Override
	public OrderDetail findById(String id) {
		OrderDetail orderDetail = orderDetailRepository.findById(id).get();
		return orderDetail;
	}

	@Override
	public List<OrderDetail> findByOrderId(String id) {
		List<OrderDetail> list = orderDetailRepository.findByOrderId(id);
		return list;
	}

}
