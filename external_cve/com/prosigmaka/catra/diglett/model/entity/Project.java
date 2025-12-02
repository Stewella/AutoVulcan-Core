package com.prosigmaka.catra.diglett.model.entity;

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
@Table(name = "t_project")
public class Project {
    @Id
    @GeneratedValue(generator = "project-generator")
    @GenericGenerator(name = "project-generator",
            parameters = @Parameter(name="prefix", value="PRO"),
            strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
    @Column(name = "proid")
    private String idProject;

    @Column(name = "proname", nullable = false, unique = true)
    private String nameProject;

    @Column(name = "propic", nullable = false)
    private String picProject;

    @ManyToOne
    @JoinColumn(name = "cliid", insertable = false, updatable = false)
    private Client client;

    @Column(name = "cliid", nullable = false)
    private String idClient;

}
