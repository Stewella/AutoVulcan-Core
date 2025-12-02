package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Data
@Entity
@Table (name="t_posisi")
public class Posisi {
    @Id
	@GeneratedValue(generator = "posisi-generator")
	@GenericGenerator(name = "posisi-generator",
	parameters = @Parameter(name="prefix", value="PSS"),
	strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
	@Column(name = "posid")
    private String id;
    @Column(name = "posnm", unique = true, nullable = false)
    private String posisi;
    // @Column(name = "cluid", nullable = false)
    // private String idCluster;
    // @ManyToOne
    // @JoinColumn (name = "cluid", insertable = false, updatable = false)
    // private Cluster cluster;
}
