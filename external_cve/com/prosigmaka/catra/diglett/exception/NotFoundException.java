package com.prosigmaka.catra.diglett.exception;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {

	private String errMsg;

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Exception e) {
		super(message);
		this.errMsg = e.getMessage();
		super.setStackTrace(e.getStackTrace());
	}

}
