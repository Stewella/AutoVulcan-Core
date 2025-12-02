package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;

@Data
public class Email {
	private String emailName;
	private String callback;
	private String receiver;
	private String subject;
	private String body;
	private String multiPartFile;
	private String response;
}
