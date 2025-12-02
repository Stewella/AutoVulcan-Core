package com.prosigmaka.catra.diglett.model.dto.response;

/*
 * Copyright(c) 2020. All rights reserved.
 * Last modified 08/05/20 08.29
 */

import lombok.*;
import org.keycloak.representations.AccessTokenResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetapodAccessTokenResponse {
    private int status;
    private String message;
    private AccessTokenResponse data;
}
