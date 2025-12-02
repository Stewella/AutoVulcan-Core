package com.prosigmaka.catra.diglett.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserLockCandidateResponse {

    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @JsonProperty(value = "lock_start")
    private java.util.Date tglMulai;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @JsonProperty(value = "lock_end")
    private java.util.Date tglSelesai;

    @JsonProperty(value = "nama_lengkap")
    private String nama;

    @JsonProperty(value = "jk")
    private String jenisKelamin;
}
