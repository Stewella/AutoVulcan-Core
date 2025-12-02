package com.prosigmaka.catra.diglett.model.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

/* Entity Step berisi keseluruhan tahapan-tahapan yang mungkin saja ada pada proses rekrutmen.
   Sebagai contoh, proses rekrutmen project A memiliki tahapan live coding, interview user;
                   proses rekrutmen project B memiliki tahapan psikotes, live coding, interview HRD, interview user.
   Maka, t_step berisi live coding, interview user, psikotes, interview HRD. */

@Data
@Entity
@Table(name="t_step")
public class Step {
    @Id
    @GeneratedValue(generator = "step-generator")
    @GenericGenerator(name = "step-generator", 
    parameters = @Parameter(name = "prefix", value = "STP"), 
    strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
    @Column(name = "stepid")
    private String idStep;
    
    @Column(name="stepnm", nullable=false, unique=true)
    private String namaStep;
}
