package com.prosigmaka.catra.diglett.model.entity;

import com.prosigmaka.catra.diglett.base.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;

/*
 * Copyright (c) 2021. All rights reserved.
 * Rosa Nur Pranita
 */

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "t_lockUser")
@Where(clause = "deleted_date is null")
public class LockUser extends BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "psm_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Integer id;

	@Column(name = "tgl_mulai", nullable = false)
	private Date tglMulai;

	@Column(name = "tgl_selesai", nullable = false)
	private Date tglSelesai;

	@ManyToOne
	@JoinColumn(name = "cndid", insertable = false, updatable = false)
	private Candidate candidate;

	@Column(name = "cndid", nullable = false)
	private String candidateId;

	@ManyToOne
	@JoinColumn(name = "dkbid", insertable = false, updatable = false)
	private DetailKebutuhan detailKebutuhan;

	@Column(name = "dkbid", nullable = false)
	private String detkebId;

	@Column(name = "keterangan", columnDefinition = "TEXT")
	private String keterangan;

}
