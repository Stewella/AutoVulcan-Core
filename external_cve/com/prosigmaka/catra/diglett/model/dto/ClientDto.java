package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private String id;
    private String nama;
    private String industri;
    private String alamat;
    private String email;
    private String noHp;
    private String sales;
    private String nickname;
}
