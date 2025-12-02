package com.prosigmaka.catra.diglett.model.dto;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLockDto implements Serializable {
    private String tglMulai;
    private String tglSelesai;
    private String candidateId;
}
