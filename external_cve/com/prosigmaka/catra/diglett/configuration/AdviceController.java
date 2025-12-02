package com.prosigmaka.catra.diglett.configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AdviceController{

	@ExceptionHandler(Exception.class)
	public @ResponseBody DefaultResponse<String> handleException(Exception e) {
		return DefaultResponse.error(e.getMessage());
	}
	
}
