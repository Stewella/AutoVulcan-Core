package com.prosigmaka.catra.diglett.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name = "t_pic_client")
public class PicClient {
	@Id
	@GeneratedValue(generator = "picclient-generator")
	@GenericGenerator(name = "picclient-generator", 
	parameters = @Parameter(name = "prefix", value = "PIC"), 
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "picid")
	private String id;

	@Column(name = "picname", nullable = false, unique = true)
	private String picName;

	@Column(name = "picemail", nullable = false, unique = true)
	private String picEmail;

	@Column(name = "picNoHp", nullable = false, unique = true)
	private String picNoHp;

	@ManyToOne
	@JoinColumn(name = "cliid", insertable = false, updatable = false)
	private Client client;
	@Column(name = "cliid", nullable = false)
	private String idClient;

	@Column(name = "kbclstatus")
	private String isOpen;
}
