package com.prosigmaka.catra.diglett.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosigmaka.catra.diglett.model.dto.CandidateDto;
import lombok.*;

/*
 * Copyright (c) 2021. All rights reserved.
 * Rosa Nur Pranita
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserLockResponse {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @JsonProperty(value = "lock_start")
    private java.util.Date tglMulai;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @JsonProperty(value = "lock_end")
    private java.util.Date tglSelesai;

    @JsonProperty(value = "canidate")
    private CandidateDto candidate;
}
