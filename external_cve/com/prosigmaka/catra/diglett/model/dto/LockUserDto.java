package com.prosigmaka.catra.diglett.model.dto;

import lombok.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockUserDto implements Serializable {
	private Integer id;
    private Date tglMulai;
    private Date tglSelesai;
    private String candidateId;
    private String kodeDetkeb;
    private String keterangan;
}
