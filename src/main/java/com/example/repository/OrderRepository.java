package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	
	@Query(value = "select MONTH(o.order.createDay), SUM(o.totail) "
			+ "from OrderDetail o "
			+ "where YEAR(o.order.createDay) = :year "
			+ "group by MONTH(o.order.createDay) "
			+ "order by MONTH(o.order.createDay) asc")
	List<Object[]> findByReportMonthObject(@Param("year")int year);
	
	@Query(value = "select YEAR(o.order.createDay) "
			+ "from OrderDetail o "
			+ "group by YEAR(o.order.createDay) "
			+ "order by YEAR(o.order.createDay) asc")
	List<Integer> findByYear();

	@Query(value = "select o.order.id, o.order.deliveryAddress, SUM(o.quantity), "
			+ "SUM(o.totail), o.order.createDay, o.order.status, o.order.pay "
			+ "from OrderDetail o "
			+ "where o.order.user.id = :id "
			+ "group by o.order.id, o.order.deliveryAddress, "
			+ "o.order.createDay, o.order.status, o.order.pay")
	List<Object[]> findOrderByUserId(@Param("id")String id, Sort sort);
	
	@Query(value = "select "
			+ "o.order.id, o.order.user.fullName, o.order.user.email, "
			+ "o.order.deliveryAddress, o.order.createDay, SUM(o.quantity),"
			+ "SUM(o.totail), o.order.status, o.order.pay "
			+ "from OrderDetail o "
			+ "where o.order.user.fullName like :keyword "
			+ "group by o.order.id, o.order.user.fullName, o.order.user.email, "
			+ "o.order.deliveryAddress, o.order.createDay, "
			+ "o.order.status, o.order.pay")
	Page<Object[]> findByInvoiceObject(@Param("keyword") String invoicepagekeyword, Pageable pageable);
}
