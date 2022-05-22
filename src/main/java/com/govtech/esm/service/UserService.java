package com.govtech.esm.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import com.govtech.esm.entity.UserDetail;
import com.govtech.esm.exception.Errors;
import com.govtech.esm.exception.ServiceException;
import com.govtech.esm.repository.UserDetailsRepository;
import com.govtech.esm.util.Constants;
import com.govtech.esm.util.DateUtil;
import com.govtech.esm.vo.FileUploadResult;
import com.govtech.esm.vo.ListUserCriteria;
import com.govtech.esm.vo.UserSearchFilter;
import com.govtech.esm.vo.UserSearchFilters;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Class.
 * 
 * @author vanhoang.nguyen
 *
 */
@Service
@Transactional
@Slf4j
public class UserService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private Validator beanValidator;

	public UserService(UserDetailsRepository userRepository) {
		this.userDetailsRepository = userRepository;
	}

	/**
	 * UploadUsers
	 * 
	 * @param fileUploadResult
	 * @return
	 */
	public HttpStatus uploadUsers(FileUploadResult fileUploadResult) {

		log.info("Start uploadUsers.");

		HttpStatus status;

		boolean hasChanges = false;

		List<UserDetail> userDetails = fileUploadResult.getUserDetails();

		if (userDetails != null) {
			// Loop to insert/update for all rows
			for (UserDetail userDetail : userDetails) {
				try {
					UserDetail userDetailFromDB = userDetailsRepository.findById(userDetail.getId()).orElse(null);

					// create or update user
					if (userDetailFromDB == null || !userDetailFromDB.equals(userDetail)) {
						hasChanges = true;
						validateUser(userDetail);
						userDetailsRepository.save(userDetail);
					}

					userDetailsRepository.flush();
					
				} catch (DataIntegrityViolationException e) {
					// unique column
					throw new ServiceException(Errors.USER_FILE_LOGIN_UNIQUE_CONSTRAINT, "(" + userDetail.getLogin() + ")", null);
				} catch (Exception e) {
					throw new ServiceException(Errors.USER_FILE_UPLOAD_FAILED, "(" + userDetail.getLogin() + ")", null);
				}
			}
		}

		// if has change
		status = hasChanges ? HttpStatus.CREATED : HttpStatus.OK;

		log.info("End uploadUsers. status {}", status);
		return status;
	}

	/**
	 * getUserDetail
	 * 
	 * @param id
	 * @return
	 */
	public com.govtech.esm.model.UserDetail getUserDetail(String id) {

		log.info("Start getUserDetail. Id = {}", id);

		com.govtech.esm.model.UserDetail userDetailModel = userDetailsRepository.findById(id).map(this::entityToModel)
				.orElseThrow(() -> new ServiceException(Errors.USER_DATA_CRUD_USER_NOT_EXIST, "(" + id + ")", null));

		log.info("End getUserDetail.");

		return userDetailModel;
	}

	/**
	 * Delete A User
	 * 
	 * @param id
	 */
	public void deleteUser(String id) {

		log.info("Start deleteUser. Id = {}", id);

		UserDetail userDetail = userDetailsRepository.findById(id)
				.orElseThrow(() -> new ServiceException(Errors.USER_DATA_CRUD_USER_NOT_EXIST, "(" + id + ")", null));

		userDetailsRepository.delete(userDetail);

		log.info("End deleteUser.");
	}

	/**
	 * Create User
	 * 
	 * @param userDetailModel
	 */
	public void createUser(com.govtech.esm.model.UserDetail userDetailModel) {

		log.info("Start createUser. userDetailModel={}", userDetailModel);

		UserDetail userDetail = modelToEntity(userDetailModel);

		// Validate the user
		validateUser(userDetail);

		UserDetail userDetailFromDB = userDetailsRepository.findById(userDetail.getId()).orElse(null);

		// Check if user exist -->cannot create new and throw exception
		if (userDetailFromDB != null) {

			throw new ServiceException(Errors.USER_DATA_CRUD_USER_ALREADY_EXIST, "(" + userDetail.getId() + ")", null);

		} else {
			// If Login exist, throw exception
			userDetailFromDB = userDetailsRepository.findByLogin(userDetail.getLogin()).orElse(null);
			if (userDetailFromDB != null) {
				// Login is unique column
				throw new ServiceException(Errors.USER_FILE_LOGIN_UNIQUE_CONSTRAINT, "(" + userDetail.getLogin() + ")", null);
			}
		}

		userDetailsRepository.save(userDetail);

		log.info("End createUser.");

	}

	/**
	 * Update user detail
	 * 
	 * @param userDetailModel
	 */
	public void updateUser(com.govtech.esm.model.UserDetail userDetailModel) {

		log.info("Start updateUser.userDetailModel= {}", userDetailModel);

		UserDetail userDetail = modelToEntity(userDetailModel);

		validateUser(userDetail);

		// If user does not exist, throw exception
		userDetailsRepository.findById(userDetail.getId())
				.orElseThrow(() -> new ServiceException(Errors.USER_DATA_CRUD_USER_NOT_EXIST, "(" + userDetail.getId() + ")", null));

		UserDetail userDetailFromDB = userDetailsRepository.findByLogin(userDetail.getLogin()).orElse(null);
		if (userDetailFromDB != null && !userDetailFromDB.getId().equalsIgnoreCase(userDetail.getId())) {
			// Login is unique column
			throw new ServiceException(Errors.USER_FILE_LOGIN_UNIQUE_CONSTRAINT, "(" + userDetail.getLogin() + ")", null);
		}

		userDetailsRepository.save(userDetail);

		log.info("End updateUser.");

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void validateUser(UserDetail user) {

		log.info("Start validateUser.user = {}", user);

		org.springframework.validation.Errors errors = new BindException(user, "user");

		beanValidator.validate(user, errors);

		if (errors.hasErrors()) {
			String errorMessage = errors.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));

			throw new ServiceException(Errors.USER_DATA_INVALID, errorMessage, null);

		}

		log.info("End validateUser.");
	}

	/**
	 * Search user by search criteria. We do not Transaction for this search so put
	 * Propagation.SUPPORTS
	 * 
	 * @param listUserCriteria
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<com.govtech.esm.model.UserDetail> listUser(ListUserCriteria listUserCriteria) {

		BigDecimal minSalary = listUserCriteria.getMinSalary();
		BigDecimal maxSalary = listUserCriteria.getMaxSalary();
		int offset = listUserCriteria.getOffset();
		int limit = listUserCriteria.getLimit() == 0 ? Integer.MAX_VALUE : listUserCriteria.getLimit();

		// Sort
		String sortName = listUserCriteria.getSortName();
		String sortDir = listUserCriteria.getSortDir();

		List<String> sortFields = Arrays.asList("id", "login", "name", "salary", "startDate");
		int sortIndex = (sortFields.contains(sortName.toLowerCase())) ? sortFields.indexOf(sortName.toLowerCase()) + 1 : 1;

		// Filter
		String idFilter = null;
		String loginFilter = null;
		String nameFilter = null;
		BigDecimal salaryFilter = null;
		Date startDateFilter = null;
		UserSearchFilters userSearchFilters = listUserCriteria.getUserSearchFilters();

		// Check if having any filter
		if (userSearchFilters != null && userSearchFilters.getFilters() != null) {

			List<UserSearchFilter> filters = userSearchFilters.getFilters();
			for (UserSearchFilter userSearchFilter : filters) {

				String filterName = userSearchFilter.getFilterName();
				String filterValue = userSearchFilter.getFilterValue();

				switch (filterName.toLowerCase()) {
				case "id":
					idFilter = filterValue;
					break;
				case "login":
					loginFilter = filterValue;
					break;
				case "name":
					nameFilter = filterValue;
					break;
				case "salary":
					salaryFilter = new BigDecimal(filterValue);
					break;
				case "startdate":
					startDateFilter = DateUtil.parse(filterValue, Constants.DATE_FORMAT_1);
					break;
				default:
					break;
				}

			}
		}

		List<UserDetail> listUser;
		// Search DESC
		if ("DESC".equalsIgnoreCase(sortDir)) {

			listUser = userDetailsRepository.findUsersDesc(minSalary, maxSalary, idFilter, loginFilter, nameFilter, salaryFilter, startDateFilter,
					sortIndex, offset, limit);

		} else {// Search ASC
			listUser = userDetailsRepository.findUsersAsc(minSalary, maxSalary, idFilter, loginFilter, nameFilter, salaryFilter, startDateFilter,
					sortIndex, offset, limit);
		}

		return listUser.stream().map(UserService::convertToDTO).collect(Collectors.toList());

	}

	private static com.govtech.esm.model.UserDetail convertToDTO(UserDetail userDetail) {
		return new com.govtech.esm.model.UserDetail()
				.id(userDetail.getId())
				.login(userDetail.getLogin())
				.name(userDetail.getName())
				.salary(userDetail.getSalary().doubleValue())
				.startDate(DateUtil.format(userDetail.getStartDate(), Constants.DATE_FORMAT_1));
	}

	private com.govtech.esm.model.UserDetail entityToModel(UserDetail userDetail) {
		return convertToDTO(userDetail);
	}

	private UserDetail modelToEntity(com.govtech.esm.model.UserDetail userDetailModel) {

		Date dateFromString = DateUtil.parse(userDetailModel.getStartDate(), Constants.DATE_FORMAT_1);
		if (dateFromString == null) {
			throw new ServiceException(Errors.USER_DATA_CRUD_START_DATE_NOT_CORRECT_FORMAT, "(" + userDetailModel.getStartDate() + ")", null);
		}

		UserDetail userDetail = new UserDetail();
		userDetail.setId(userDetailModel.getId());
		userDetail.setLogin(userDetailModel.getLogin());
		userDetail.setName(userDetailModel.getName());
		userDetail.setSalary(BigDecimal.valueOf((userDetailModel.getSalary())));
		userDetail.setStartDate(dateFromString);
		return userDetail;
	}
}
