package com.govtech.esm.vo;

import com.govtech.esm.entity.UserDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileUploadResult implements Serializable {

	private static final long serialVersionUID = 4946343335601512014L;

	private List<UserDetail> userDetails;

	private List<FileValidationError> fileValidationErrors;

	public FileUploadResult() {
		userDetails = new ArrayList<>();

		fileValidationErrors = new ArrayList<>();
	}

	public void addUserDetail(UserDetail userDetail) {
		userDetails.add(userDetail);
	}

	public void addFileValidationError(FileValidationError fileValidationError) {
		fileValidationErrors.add(fileValidationError);
	}

	public boolean hasErrors() {
		return fileValidationErrors != null && !fileValidationErrors.isEmpty();
	}

	public String getErrorMessages() {
		if (hasErrors()) {
			StringBuilder builder = new StringBuilder();
			fileValidationErrors.forEach(fileValidationError ->
					builder.append("Line " + fileValidationError.getLineNumber() + ":" + fileValidationError.getErrorMessage() + ". "));
			return builder.toString();
		}

		return "";
	}

}
