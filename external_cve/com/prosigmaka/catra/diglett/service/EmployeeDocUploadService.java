package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.entity.EmployeeDocUpload;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeDocUploadService {
    EmployeeDocUpload insertData(EmployeeDocUpload entity, MultipartFile file, String folderStrPath);
    String inserFile(EmployeeDocUpload entity, MultipartFile file, String folderStrPath);
    EmployeeDocUpload deleteData(EmployeeDocUpload entity);

}
