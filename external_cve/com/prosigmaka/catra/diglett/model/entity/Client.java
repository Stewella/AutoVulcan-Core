package com.prosigmaka.catra.diglett.model.entity;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
@Table(name = "t_client")
public class Client {
	@Id
	@GeneratedValue(generator = "client-generator")
	@GenericGenerator(name = "client-generator", 
	parameters = @Parameter(name = "prefix", value = "CL"), 
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "cliid")
	private String id;

	@Column(name = "clinm", nullable = false, unique = true)
	private String nama;

	@ManyToOne
	@JoinColumn(name = "indid", insertable = false, updatable = false)
	private Industry Industry;
	@Column(name = "indid", nullable = false)
	private Integer idIndustri;

	@Column(name = "cliad", nullable = false)
	private String alamat;
	
	@Column(name="clemail", nullable = false, unique = true)
	private String clEmailPrimary;
	
	@Column(name="clNoHp", nullable = false, unique = true)
	private String clNoHpPrimary;
	
//	@Column(name="sales", nullable = false)
//	private String sales;
	
	@ManyToOne
	@JoinColumn(name = "salesid", insertable = false, updatable = false)
	private Sales sales;
	@Column(name = "salesid", nullable = false)
	private Integer idSales;
//	
	@Column(name="shclinm", nullable = false, unique = true)
	private String nickname;

}
