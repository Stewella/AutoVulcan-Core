package com.prosigmaka.catra.diglett.model.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name="t_step_detail")
public class StepDetail {
    @Id
    @GeneratedValue(generator = "stepdet-generator")
    @GenericGenerator(name = "stepdet-generator", 
    parameters = @Parameter(name = "prefix", value = "STPDET"), 
    strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
    @Column(name = "stepdetid")
    private String idStepDet;
    
    @ManyToOne
    @JoinColumn(name = "stepid", nullable=false)
    private Step step;

    // @Column(name = "stepid", nullable=false)
    // private String stepId;
    
    @ManyToOne
    @JoinColumn(name = "cliid", nullable=false)
    private Client client;

    // @Column(name = "proid", nullable=false)
    // private String projectId;

    @Column(name = "steporder", nullable = false)
    private Integer orderStep;
}

