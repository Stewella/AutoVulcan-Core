package com.prosigmaka.catra.diglett.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "t_det_keb")
public class DetailKebutuhan {
	@Id
	@GeneratedValue(generator = "detkeb-generator")
	@GenericGenerator(name = "detkeb-generator", 
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "dkbid")
	private String id;
	
	@Column(name="kode", nullable = true, unique = true)
	private String kode;
	
	@ManyToOne
	@JoinColumn(name = "picid", insertable = false, updatable = false)
	private PicClient picClient;
	@Column(name = "picid", nullable = false)
	private String idPic;

	@ManyToOne
	@JoinColumn(name = "posid", insertable = false, updatable = false)
	private Posisi posisi;
	@Column(name = "posid", nullable = false)
	private String idPosisi;
	
	@ManyToOne
	@JoinColumn(name = "servid", insertable = false, updatable = false)
	private Services services;
	@Column(name = "servid", nullable = false)
	private String idService;

	@Column(name="level", nullable = true)
	private String level;
	
	@Column(name="jumlah", nullable = false)
	private Integer jumlah;
}
