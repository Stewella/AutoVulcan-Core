package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PicClientDto {
    private String id;
    private String picName;
    private String picEmail;
    private String picNoHp;
    private String client;
    private String isOpen;

}
