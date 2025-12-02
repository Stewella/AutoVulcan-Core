package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.entity.EmployeeDocUpload;
import com.prosigmaka.catra.diglett.repository.EmployeeDocUploadRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class EmployeeDocUploadServiceImpl implements EmployeeDocUploadService {

    private final EmployeeDocUploadRepository employeeDocUploadRepository;

    public EmployeeDocUploadServiceImpl(EmployeeDocUploadRepository employeeDocUploadRepository) {
        this.employeeDocUploadRepository = employeeDocUploadRepository;
    }

    @Override
    public EmployeeDocUpload insertData(EmployeeDocUpload entity, MultipartFile file, String folderStrPath) {
        String fileName = inserFile(entity, file, folderStrPath);
        String url = folderStrPath + "/" + fileName;
        entity.setIsDelete(0);
        entity.setNamaFile(fileName);
        entity.setUrlFile(url);
        entity = employeeDocUploadRepository.save(entity);
        return entity;
    }

    @Override
    public String inserFile(EmployeeDocUpload entity, MultipartFile file, String folderStrPath) {
        String name = entity.getCandidate().getNama();
        String idCandidate = entity.getCandidate().getId();
        String docType = entity.getDocumentType().getNamaDocument();
        String[] splitName = name.split(" ");
        if (splitName.length > 1) {
            name = name.replace(" ", "-");
        } else {
            name = splitName[0];
        }

        Path folderPath = Paths.get(folderStrPath);
        String fileName = idCandidate + "_" + docType + "_" + name;
        String format = ".pdf";
        String withFormat = fileName + format;

        File existFile = new File(folderStrPath + "/" + withFormat);
        int index = 1;

        while (existFile.exists()) {
            String tampung = fileName + "_" + index;
            index++;
            withFormat = tampung + format;
            existFile = new File(folderStrPath + "/" + withFormat);
        }

        Path path = folderPath.resolve(withFormat);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return withFormat;
    }

    @Override
    public EmployeeDocUpload deleteData(EmployeeDocUpload entity) {
        entity.setIsDelete(1);
        return entity;
    }


}
