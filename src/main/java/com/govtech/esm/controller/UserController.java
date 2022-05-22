package com.govtech.esm.controller;

import com.govtech.esm.api.UsersApi;
import com.govtech.esm.model.ListUserResponse;
import com.govtech.esm.model.UserDetail;
import com.govtech.esm.model.UserResponse;
import com.govtech.esm.service.UserService;
import com.govtech.esm.util.CSVParserUtil;
import com.govtech.esm.util.CommonUtil;
import com.govtech.esm.vo.FileUploadResult;
import com.govtech.esm.vo.ListUserCriteria;
import com.govtech.esm.vo.UserSearchFilters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

/**
 * RestControll Class to handle requests to user.
 * 
 * @author vanhoang.nguyen
 *
 */
@RestController
@CrossOrigin()
public class UserController implements UsersApi {

	@Autowired
	private UserService userService;

	@Autowired
	private CSVParserUtil csvParserUtil;

	@Override
	public ResponseEntity upload(@Valid MultipartFile fileName) throws IOException {

		// Parse the CSV file first
		FileUploadResult fileUploadResult = csvParserUtil.parseCSVFile(fileName);

		// If no error then proceed calling to service to further processing and import to DB
		HttpStatus httpStatus = userService.uploadUsers(fileUploadResult);

		return ResponseEntity.status(httpStatus).build();
	}

	@Override
	public ResponseEntity<ListUserResponse> listUsers(@Valid Double minSalary, @Valid Double maxSalary, @Valid Integer offset, @Valid Integer limit,
			@Valid String sortName, @Valid String sortDir, @Valid String filters) {

		ListUserResponse listUserResponse = new ListUserResponse();

		ListUserCriteria listUserCriteria = new ListUserCriteria();
		listUserCriteria.setMinSalary(BigDecimal.valueOf(minSalary));
		listUserCriteria.setMaxSalary(BigDecimal.valueOf(maxSalary));
		listUserCriteria.setOffset(offset);
		listUserCriteria.setLimit(limit);
		listUserCriteria.setSortName(sortName);
		listUserCriteria.setSortDir(sortDir);

		UserSearchFilters userSearchFilters = CommonUtil.parseFilters(filters);
		listUserCriteria.setUserSearchFilters(userSearchFilters);

		// call service to search the users base on Search User Criteria
		List<UserDetail> listUser = userService.listUser(listUserCriteria);

		listUserResponse.results(listUser);
		return ResponseEntity.ok().body(listUserResponse);
	}

	@Override
	public ResponseEntity<UserDetail> getUser(String id) {

		UserDetail userDetail = userService.getUserDetail(id);

		return ResponseEntity.ok().body(userDetail);
	}

	@Override
	public ResponseEntity<UserResponse> createUser(com.govtech.esm.model.UserDetail userDetail) {

		UserResponse userResponse = new UserResponse();

		userService.createUser(userDetail);

		userResponse.setMessage("Successfully Created");
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
	}

	@Override
	public ResponseEntity<UserResponse> updateUser(@Valid UserDetail userDetail) throws Exception {

		UserResponse userResponse = new UserResponse();

		userService.updateUser(userDetail);

		userResponse.setMessage("Successfully Updated");
		return ResponseEntity.ok().body(userResponse);
	}

	@Override
	public ResponseEntity<UserResponse> deleteUser(String id) {

		UserResponse userResponse = new UserResponse();

		userService.deleteUser(id);

		userResponse.setMessage("Successfully deleted");
		return ResponseEntity.ok().body(userResponse);
	}

}
