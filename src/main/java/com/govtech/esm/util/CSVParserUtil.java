package com.govtech.esm.util;

import com.govtech.esm.entity.UserDetail;
import com.govtech.esm.exception.Errors;
import com.govtech.esm.exception.ServiceException;
import com.govtech.esm.service.UserService;
import com.govtech.esm.vo.FileUploadResult;
import com.govtech.esm.vo.FileValidationError;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This Class to parse the CSV file. While parsing, system will also do some
 * format validation.
 * 
 * @author vanhoang.nguyen
 *
 */
@Component
public class CSVParserUtil {

	public static final String CSV_TYPE = "text/csv";

	@Autowired
	UserService useService;

	/**
	 * Parse CSV File
	 * 
	 * @param file
	 * @return
	 */
	public synchronized FileUploadResult parseCSVFile(MultipartFile file) {

		FileUploadResult fileUploadResult = csvToUserDetails(file);

		return fileUploadResult;
	}

	/**
	 * Parse the CSV file to list of User Detail. While parsing, system also does
	 * format validations.
	 * 
	 * @param file
	 * @return
	 */
	private FileUploadResult csvToUserDetails(MultipartFile file) {

		FileUploadResult fileUploadResult = new FileUploadResult();
		List<UserDetail> userDetails = fileUploadResult.getUserDetails();
		Set<String> userIdSet = new HashSet<>();

		// Check if Content Type is text/csv
		if (!CSV_TYPE.equals(file.getContentType())) {

			throw new ServiceException(Errors.USER_FILE_NOT_CORRECT_FORMAT, "File is not in correct format", null);

		} else {

			try {
				BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));

				// Use Apache Common CSV to parse the file
				// # to comment line
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withCommentMarker('#'));

				List<CSVRecord> csvRecords = csvParser.getRecords();
				for (CSVRecord csvRecord : csvRecords) {

					String id = csvRecord.get("id");
					String login = csvRecord.get("login");
					String name = csvRecord.get("name");
					String salary = csvRecord.get("salary");
					String startDate = csvRecord.get("startDate");

					BigDecimal salaryBigDecimal;
					Date startDateDate;
					try {

						// CHeck if any duplication within the file.
						if (!userIdSet.contains(id)) {

							// Salary in decimal that is >= 0.0
							try {
								salaryBigDecimal = new BigDecimal(salary);
							} catch (NumberFormatException e) {
								throw new ServiceException(Errors.USER_FILE_SALARY_NOT_CORRECT_FORMAT, "(" + salary + ")", null);
							}

							// StartDate format: yyyy-MM-dd or dd-MMM-yy
							startDateDate = DateUtil.parse(startDate, Constants.DATE_FORMAT_1);
							if (startDateDate == null) {
								startDateDate = DateUtil.parse(startDate, Constants.DATE_FORMAT_2);
							}

							if (startDateDate == null) {
								throw new ServiceException(Errors.USER_FILE_START_DATE_NOT_CORRECT_FORMAT, "(" + startDate + ")", null);
							}

							UserDetail userDetail = new UserDetail();
							userDetail.setId(id);
							userDetail.setLogin(login);
							userDetail.setName(name);
							userDetail.setSalary(salaryBigDecimal);
							userDetail.setStartDate(startDateDate);

							// Validate User
							useService.validateUser(userDetail);

							userDetails.add(userDetail);

							userIdSet.add(id);

						} else {// duplicated row within CSV file
							throw new ServiceException(Errors.USER_FILE_DUPLICATED_RECORDS);
						}

					} catch (ServiceException e) {

						// Collect all the errors in the file.
						FileValidationError fileValidationError = new FileValidationError();

						fileValidationError.setLineNumber(csvRecord.getRecordNumber());
						fileValidationError.setErrorCode(e.getError().getCode());
						fileValidationError.setErrorMessage(e.getMessage());

						fileUploadResult.addFileValidationError(fileValidationError);

					}
				}
			} catch (Exception e) {

				// Any unexpected error
				throw new ServiceException(Errors.USER_FILE_UPLOAD_FAILED, "Error when processing the line. ", e);
			}

			// if there is any error, throw Service Exception
			if (fileUploadResult.hasErrors()) {
				throw new ServiceException(Errors.USER_FILE_UPLOAD_FAILED, fileUploadResult.getErrorMessages(), null);
			}

			return fileUploadResult;
		}
	}
}