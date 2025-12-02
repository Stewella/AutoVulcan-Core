package com.prosigmaka.catra.diglett.model.enummodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PicClientEnum {
	TRUE("True"),
	FALSE("False");

	private final String value;

}
