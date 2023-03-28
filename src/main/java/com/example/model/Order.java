package com.example.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id")
	private String id;

	@Column(name = "delivery_address")
	private String deliveryAddress;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "pay")
	private String pay;

	@Column(name = "create_day")
	private Date createDay;

	@Column(name = "update_date")
	private Date updateDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnore
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;
}
