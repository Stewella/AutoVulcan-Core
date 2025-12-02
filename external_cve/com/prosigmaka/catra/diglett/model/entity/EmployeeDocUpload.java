package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "t_employe_upload")
public class EmployeeDocUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUpload")
    private Integer idUpload;

    @ManyToOne
    @JoinColumn(name = "idDocType", insertable = false, updatable = false)
    private DocumentType documentType;
    @Column(name = "idDocType")
    private Integer idDocType;

    @ManyToOne
    @JoinColumn(name = "cndid", insertable = false, updatable = false)
    private Candidate candidate;
    @Column(name = "cndid")
    private String id;

    private String namaFile;
    private String urlFile;
    private String keterangan;
    private Integer isDelete;
}
