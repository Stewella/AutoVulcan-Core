package com.prosigmaka.catra.diglett.model.entity;

import java.sql.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name="t_candidate")
public class Candidate {
	@Id
	@GeneratedValue(generator = "candidate-generator")
	@GenericGenerator(name = "candidate-generator",
	parameters = @Parameter(name="prefix", value="PSM"),
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "cndid")
	private String id;
	
	@Column(name="cndnm", nullable = false)
	private String nama;
	
	@Column(name="cndkode", nullable = true, unique = true)
	private String cndkode;
	
	@Column(name="cndjk")
	private String jenisKelamin;
	
	@Column(name="cndtmplahir")
	private String tempatLahir;
	
	@Column(name="cndtgllahir")
	private Date tanggalLahir;
	
	@Column(name="cndalamat")
	private String alamat;
	
	@Column(name="cdnemail",unique = true)
	private String emailPrimary;
	
	@Column(name="noHp", unique = true)
	private String noHpPrimary;

	@Column(name="cndavail")
	private String availKandidat;
	
	@Column(name="ekspektasiGaji")
	private String ekspektasiGaji;
	
	@Column(name="waktuAvailable")
	private String waktuAvailable;
	
	@Column(name="tanggalProses")
	private Date tanggalProses;

	@OneToOne
	@JoinColumn(name="idEmployee", nullable = true)
	private Employee employee;

	//for delete
	@Column(name = "isDelete")
	private Integer isDelete;

}
