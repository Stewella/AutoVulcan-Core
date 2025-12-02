package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Data
@Entity
@Table(name = "t_recposisi")
public class RecommendPosisi {
	@Id
	@GeneratedValue(generator = "recposisi-generator")
	@GenericGenerator(name = "recposisi-generator",
	parameters = @Parameter(name = "prefix", value = "REC"),
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "recid")
	private String id;
	@Column(name = "cndid", nullable = false)
	private String idCandidate;
	@ManyToOne
	@JoinColumn(name = "cndid", insertable = false, updatable = false)
	private Candidate candidate;
	@Column(name = "posid", nullable = false)
	private String idPosisi;
	@ManyToOne
	@JoinColumn(name = "posid", insertable = false, updatable = false)
	private Posisi posisi;
	@Column(name="lamapglmn",columnDefinition="TEXT")
    private String lamapglmn;
}
