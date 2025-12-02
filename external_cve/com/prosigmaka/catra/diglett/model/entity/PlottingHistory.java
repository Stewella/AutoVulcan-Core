package com.prosigmaka.catra.diglett.model.entity;

import java.sql.Date;

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
@Table(name = "t_plt_hist")
public class PlottingHistory {
	@Id
	@GeneratedValue(generator = "plotHist-generator")
	@GenericGenerator(name = "plotHist-generator", 
	parameters = @Parameter(name = "prefix", value = "PLTHIST"), 
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "plthistid")
	private String idPltHist;

	@ManyToOne
	@JoinColumn(name = "cndid", insertable = false, updatable = false)
	private Candidate candidate;

	@Column(name = "cndid", nullable = false)
	private String idCandidate;

	@ManyToOne
	@JoinColumn(name = "dkbid", insertable = false, updatable = false)
	private DetailKebutuhan detailKebutuhan;

	@Column(name = "dkbid", nullable = true)
	private String idDetKebutuhan;

	@Column(name = "ket_plthist", columnDefinition = "TEXT")
	private String keteranganPltHist;

	@Column(name = "tgl_plthist", nullable = false, updatable = false)
	private Date tglKeputusan;

}
