package com.prosigmaka.catra.diglett.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_sales")
public class Sales {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "salid")
	private Integer id;
	
	@Column(name = "salnm", nullable = false, unique = true)
	private String nama;
	
	@Column(name="salnohp", unique = true)
	private String noHp;
	
	@Column(name="salemail", unique = true)
	private String email;
}
