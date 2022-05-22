package com.govtech.esm.exception;

public enum Errors {

	USER_FILE_NOT_CORRECT_FORMAT("ERROR_001", "File is not in CSV format"),
	USER_FILE_UPLOAD_FAILED("ERROR_002", "Error in processing the file"),
	USER_FILE_DUPLICATED_RECORDS("ERROR_003", "Duplicated Records"),
	USER_FILE_START_DATE_NOT_CORRECT_FORMAT("ERROR_005",
			"Date of start of employment in one of two formats: 'yyyy-mm-dd' or 'dd-mmm-yy'"),
	USER_FILE_LOGIN_UNIQUE_CONSTRAINT("ERROR_006", "Employee login not unique"),
	USER_DATA_INVALID("ERROR_007", "User Detail invalid"),
	USER_DATA_CRUD_USER_NOT_EXIST("ERROR_008", "No such employee"),
	USER_DATA_CRUD_USER_ALREADY_EXIST("ERROR_009", "Employee ID already exists"),
	USER_FILE_SALARY_NOT_CORRECT_FORMAT("ERROR_010", "Salary in decimal that is >= 0.0"),
	USER_DATA_CRUD_START_DATE_NOT_CORRECT_FORMAT("ERROR_011", "Invalid date"),
	USER_DATA_CRUD_FILTER_NOT_CORRECT_FORMAT("ERROR_012", "Filter is not in correct format (name-value,name-value)");

	private final String code;
	private final String message;

	Errors(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
