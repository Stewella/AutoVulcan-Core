package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.EmployeeDocUploadAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.EmployeeDocUploadDto;
import com.prosigmaka.catra.diglett.model.entity.EmployeeDocUpload;
import com.prosigmaka.catra.diglett.repository.EmployeeDocUploadRepository;
import com.prosigmaka.catra.diglett.service.Base64File;
import com.prosigmaka.catra.diglett.service.EmployeeDocUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:clients.properties")
@RequestMapping("/document-upload")
public class EmployeeDocUploadController {

    private final EmployeeDocUploadRepository repository;
    private final EmployeeDocUploadAssembler assembler;
    private final EmployeeDocUploadService service;
    private final Base64File base64File;
    //kalo udh di deploy folder path harus ganti
    @Value("${config.uri.docpath}")
    private String folderStrPath;
//    private final String folderStrPath = System.getProperty("user.dir").replace('\\', '/') +
//            "/src/main/resources/document";

    public EmployeeDocUploadController(EmployeeDocUploadRepository repository,
                                       EmployeeDocUploadAssembler assembler,
                                       EmployeeDocUploadService service,
                                       Base64File base64File) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
        this.base64File = base64File;
    }

    //get list document
    @GetMapping("/get-all/{id}")
    public DefaultResponse<List<EmployeeDocUploadDto>> getAll(@PathVariable String id) {
        List<EmployeeDocUpload> employeeDocList = repository.findAllByIdEqualsAndIsDeleteEquals(id, 0);
        List<EmployeeDocUploadDto> employeeDocDtoList = employeeDocList.stream()
                .map(doc -> assembler.fromEntity(doc))
                .collect(Collectors.toList());
        if (employeeDocDtoList == null)
            return DefaultResponse.error("Data tidak tersedia");

        return DefaultResponse.ok(employeeDocDtoList);
    }


    //insert document
    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefaultResponse<EmployeeDocUpload> insertData(@RequestPart(value = "upload", required = true) EmployeeDocUploadDto dto,
                                                         @RequestPart(value = "file", required = true) MultipartFile file) {

        EmployeeDocUpload entity = assembler.fromDto(dto);
        if (file.getSize() > 2000000) {
            return DefaultResponse.error("Ukuran file maksimal 2Mb");
        } else {
            entity = service.insertData(entity, file, folderStrPath);
        }

        if (entity == null)
            return DefaultResponse.error("Gagal menyimpan");

        return DefaultResponse.ok(entity, "Berhasil menyimpan");
    }

    // delete file
    @PostMapping("/delete/{id}")
    public DefaultResponse<EmployeeDocUpload> deleteData(@PathVariable Integer id) {
        EmployeeDocUpload entity = repository.findByIdUpload(id);
        entity = service.deleteData(entity);

        if (entity == null)
            return DefaultResponse.error("Gagal menghapus");

        return DefaultResponse.ok(entity, "Berhasil menghapus");

    }

    //Get pdf file
    @GetMapping("/file/{id}")
    public DefaultResponse<String> getFile(@PathVariable Integer id) {
        String fileName = repository.findByIdUpload(id).getNamaFile();
        String fileB64 = base64File.getFile(fileName, folderStrPath);
        if (fileB64 == null) {
            return DefaultResponse.error("Gagal menampilkan");
        }
        return DefaultResponse.ok(fileB64);
    }
}
