package com.prosigmaka.catra.diglett.model.dto.request;

import com.prosigmaka.catra.diglett.model.entity.Candidate;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/*
 * Copyright (c) 2021. All rights reserved.
 * Rosa Nur Pranita
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserLockRequest implements Serializable {

    private Date tglMulai;
    private Date tglSelesai;
    private Candidate candidate;
}
