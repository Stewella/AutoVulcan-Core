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
@Table(name = "t_cndhist")
public class CandidateStatusHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cndhistid")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "cndid", insertable = false, updatable = false)
	private Candidate candidate;
	@Column(name = "cndid", nullable = false)
	private String cndid;

	@Column(name = "cnd_avail_hist", nullable = false)
	private String avail;

	@Column(name = "createdBy", nullable = true)
	private String createdBy;

	@Column(name = "tanggalProses", nullable = false)
	private Date tanggalProses;
	
	@Column(name = "keterangan", columnDefinition = "TEXT")
	private String keterangan;

}
