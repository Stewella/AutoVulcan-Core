package com.prosigmaka.catra.diglett.model.entity;

import java.sql.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name="t_plotting")
public class Plotting {
    @Id
    @GeneratedValue(generator = "plotting-generator")
    @GenericGenerator(name = "plotting-generator", 
    parameters = @Parameter(name = "prefix", value = "PLT"), 
    strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
    @Column(name = "pltid")
    private String idPlotting;

    @ManyToOne
    @JoinColumn(name = "cndid", nullable = false)
    private Candidate candidate;

    // @Column(name = "cndid", nullable=false)
    // private String idCandidate;

    @ManyToOne
    @JoinColumn(name = "dkbid", nullable=false)
    private DetailKebutuhan detailKebutuhan;
    
    /* Keterangan gambaran umum tahapan yg akan si candidate lalui ini apa aja. 
       Misal, candidate A di user AX, oh kalau user AX ini lebih suka kalo nilai live codenya lebih tinggi,
       terus pas interview harus gini gini, nah itu harusnya masuk di keterangan.*/
    @Column(name="keterangan", columnDefinition="TEXT")
    private String keterangan;

    @Column(name = "createdby", nullable=true)
    private String createdBy;

    @Column(name = "createdon", nullable=false)
    private Date createdOn;
    
}
