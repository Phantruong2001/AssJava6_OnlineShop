package com.example.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.helper.StringHelper;
import com.example.model.Order;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	OrderRepository orderRepository;

	@Override
	public Order saveOrUpdate(Order entity) {
		logger.info("saveOrUpdate");
		if (StringHelper.equals(entity.getId(), "") || !StringHelper.notNull(entity.getId())) {
			entity.setCreateDay(new Date());
			entity.setUpdateDate(new Date());
		} else {
			entity.setCreateDay(findById(entity.getId()).getCreateDay());
			entity.setUpdateDate(new Date());
		}
		Order order = orderRepository.save(entity);
		return order;
	}

	@Override
	public Order findById(String id) {
		logger.info("findById");
		if (!StringHelper.notNull(id))
			return null;
		Optional<Order> order = orderRepository.findById(id);
		if (order.isEmpty())
			return null;
		return order.get();
	}

	@Override
	public Page<Object[]> findByInvoiceObject(String invoicepagekeyword, Pageable pageable) {
		logger.info("findByInvoiceObject");
		Page<Object[]> page = orderRepository.findByInvoiceObject(invoicepagekeyword, pageable);
		return page;
	}

	@Override
	public Map<String, String> findByReportMonthObject(int year) {
		logger.info("findByReportMonthObject");
		List<Object[]> list = orderRepository.findByReportMonthObject(year);
		Map<String, String> graphData = new HashMap<>();
		list.forEach(o -> {
			graphData.put("Month " + String.valueOf(o[0]), String.valueOf(o[1]));
		});
		return graphData;
	}

	@Override
	public List<Integer> findByYear() {
		logger.info("findByYear");
		List<Integer> list = orderRepository.findByYear();
		return list;
	}

	@Override
	public List<Object[]> findOrderByUserId(String id, Sort sort) {
		logger.info("findByUserId");
		List<Object[]> list = orderRepository.findOrderByUserId(id, sort);
		return list;
	}

}
