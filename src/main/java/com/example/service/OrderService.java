package com.example.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.model.Order;

public interface OrderService {

	Order saveOrUpdate(Order entity);

	Order findById(String id);
	
	Page<Object[]> findByInvoiceObject(String invoicepagekeyword, Pageable pageable);
	
	Map<String, String> findByReportMonthObject(int year);
	
	List<Integer> findByYear();
	
	List<Object[]> findOrderByUserId(String id, Sort sort);
}
