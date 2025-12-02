package com.prosigmaka.catra.diglett.exception;

import lombok.Data;

@Data
public class CommonException extends RuntimeException {

	private String errMsg;

	public CommonException(String message) {
		super(message);
	}

	public CommonException(String message, Exception e) {
		super(message);
		this.errMsg = e.getMessage();
		super.setStackTrace(e.getStackTrace());
	}

}
