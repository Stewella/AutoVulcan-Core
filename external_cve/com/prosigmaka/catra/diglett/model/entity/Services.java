package com.prosigmaka.catra.diglett.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

//import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
@Entity
@Table(name = "t_service")
public class Services {
	@Id
	@GeneratedValue(generator = "service-generator")
	@GenericGenerator(name = "service-generator",
	parameters = @Parameter(name="prefix", value="SRV"),
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "servid")
	private String id;
	@Column(name = "servnm", unique = true, nullable = false)
	private String service;
	@Column(name = "shortnm", unique = true, nullable = false)
	private String shortService;
	

}
