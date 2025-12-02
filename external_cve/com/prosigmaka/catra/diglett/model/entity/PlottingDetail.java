package com.prosigmaka.catra.diglett.model.entity;

import java.sql.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name="t_plt_detail")
public class PlottingDetail {
    @Id
    @GeneratedValue(generator = "plotDetail-generator")
    @GenericGenerator(name = "plotDetail-generator",
    parameters = @Parameter(name="prefix", value="PLTDET"),
    strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
    @Column(name = "pltdetid")
    private String idPlotDet;
    
    @ManyToOne
    @JoinColumn(name = "pltid", nullable = false)
    private Plotting plotting;

    // @Column(name = "pltid", nullable=false)
    // private String idPlotting;

    @ManyToOne
    @JoinColumn(name = "stepid", nullable=false)
    private Step step;

    // @Column(name = "stepid", nullable=false)
    // private String idStep;
    
    @Column(name="tgl")
    private Date tgl;
    
    /* Result tiap step Yes/No/Skip jika tdp step dari client
       yang tidak perlu dijalani ketika plotting kebutuhan*/
    @Column(name="temp_res")
    private String tempResult;
    
    /* temp_ket itu kayak evaluasi kandidat dari setiap tahapannya.
       Misal, pas interview, dia kurang gmn gmn nanti masuknya di temp_ket.*/
    @Column(name="temp_ket",columnDefinition="TEXT")
    private String tempKeterangan;

    @Column(name = "createdby", nullable=true)
    private String createdBy;

    @Column(name = "createdon", nullable=false)
    private Date createdOn;

}