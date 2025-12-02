package com.prosigmaka.catra.diglett.model.enummodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AvailKandidatEnum {
    AVAILABLE("Available"),
    NOT_AVAILABLE("Not Available"),
    HIRED("Hired");
	

    private final String value;
    
}
