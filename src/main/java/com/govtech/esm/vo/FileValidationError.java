package com.govtech.esm.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileValidationError implements Serializable {

	private static final long serialVersionUID = 4946343335601512014L;

	private long lineNumber;

	private String errorCode;

	private String errorMessage;

	public FileValidationError() {
	}

	public FileValidationError(int lineNumber, String errorCode, String errorMessage) {
		this.lineNumber = lineNumber;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public FileValidationError(String errorCode) {
		this.errorCode = errorCode;
	}

}
