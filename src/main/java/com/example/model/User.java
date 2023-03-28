package com.example.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.validator.BirthDay;
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
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id")
	private String id;

	@NotEmpty(message = "{NotEmpty.User.email}")
	@Email(message = "{Email.User.email}")
	@Column(name = "email")
	private String email;

	@NotEmpty(message = "{NotEmpty.User.fullName}")
	@Size(min = 5, message = "{Size.User.fullName}")
	@Column(name = "full_name")
	private String fullName;

	@NotEmpty(message = "{NotEmpty.User.address}")
	@Size(min = 10, message = "{Size.User.address}")
	@Column(name = "address")
	private String address;

	@BirthDay
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "birth_day")
	private Date birthDay;

	@Column(name = "role")
	private boolean role;
	
	@Column(name = "password")
	private String password;

	@Column(name = "account_type")
	private String accountType;

	@Column(name = "create_day")
	private Date createDay;

	@Column(name = "update_date")
	private Date updateDate;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Favority> favorites;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Share> shares;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> orders;
}
