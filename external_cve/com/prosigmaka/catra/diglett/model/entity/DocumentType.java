package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_doc_type")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDocType")
    private Integer idDocType;
    private String namaDocument;
}
