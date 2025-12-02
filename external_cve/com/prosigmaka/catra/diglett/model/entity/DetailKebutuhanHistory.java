package com.prosigmaka.catra.diglett.model.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_det_keb_hist")
public class DetailKebutuhanHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dkhid")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "dkbid", insertable = false, updatable = false)
	private DetailKebutuhan detailKebutuhan;
	@Column(name = "dkbid", nullable = false)
	private String idDetKeb;
	
	@Column(name = "dkhcreateddate")
	private Date createdDate;
	
	@Column(name = "dkhcreatedby")
	private String createdBy;
	
	@Column(name = "dkhstatus")
	private String status;
	
	@Column(name = "dkhjmlperubahan")
	private Integer jumlahPerubahan;
	
	@Column(name = "dkhcurrent")
	private Integer current;
	
	@Column(name = "dkhbefore")
	private Integer before;
	
	@Column(name = "dkhketerangan")
	private String keterangan;
	
	@Column(name = "dkhtanggalproses")
	private Date tanggalProses;
}
