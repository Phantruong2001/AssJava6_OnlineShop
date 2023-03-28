package com.example.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "send_mails")
public class SendMail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id")
	private String id;

	@Column(name = "type")
	private String type;

	@Column(name = "email_to")
	private String emailTo;

	@Column(name = "subject")
	private String subject;

	@Column(name = "code")
	private String code;

	@Column(name = "create_day")
	private Date createDay;

	@Column(name = "expiration_date")
	private Date expirationDate;
}
