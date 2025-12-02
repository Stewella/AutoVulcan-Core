package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocUploadDto {
    private Integer idUpload;
    private String namaFile;
    private String urlFile;
    private String keterangan;

    //employee
    private String id;;
    private String nama;

    //document type
    private Integer idDocType;
    private String namaDocument;

    //delete
    private Integer isDelete;

}
