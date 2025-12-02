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
@Table(name = "t_industry")
public class Industry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "indid")
	private Integer id;
	
	@Column(name = "indnm", nullable = false, unique = true)
	private String nama;
}
