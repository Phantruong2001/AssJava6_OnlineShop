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
@Table(name = "products")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "price")
	private double price;

	@Column(name = "image")
	private String image;

	@Column(name = "description")
	private String description;

	@Column(name = "discount")
	private double discount;

	@Column(name = "active")
	private boolean active;

	@Column(name = "create_day")
	private Date createDay;

	@Column(name = "update_date")
	private Date updateDate;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Favority> favorites;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Share> shares;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;
}
