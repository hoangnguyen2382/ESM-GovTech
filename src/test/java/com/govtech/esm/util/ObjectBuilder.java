package com.govtech.esm.util;

import java.math.BigDecimal;
import java.util.Date;

import com.govtech.esm.entity.UserDetail;
import com.govtech.esm.vo.UserSearchFilter;

public class ObjectBuilder {
	public static UserDetail createUser(String id, String login, String name, BigDecimal salary, Date startDate) {
		UserDetail user = new UserDetail();
		user.setId(id);
		user.setLogin(login);
		user.setName(name);
		user.setSalary(salary);
		user.setStartDate(startDate);

		return user;
	}

	public static UserSearchFilter createUserSearchFilter(String filterName, String filterValue) {
		UserSearchFilter userSearchFilter = new UserSearchFilter();
		userSearchFilter.setFilterName(filterName);
		userSearchFilter.setFilterValue(filterValue);
		return userSearchFilter;
	}

	public static com.govtech.esm.model.UserDetail createUserModel(UserDetail userDetailFromDB) {

		com.govtech.esm.model.UserDetail userDetail = new com.govtech.esm.model.UserDetail();

		userDetail.setId(userDetailFromDB.getId());
		userDetail.setLogin(userDetailFromDB.getLogin());
		userDetail.setName(userDetailFromDB.getName());
		userDetail.setSalary(userDetailFromDB.getSalary().doubleValue());
		userDetail.setStartDate(DateUtil.format(userDetailFromDB.getStartDate(), Constants.DATE_FORMAT_1));

		return userDetail;
	}
}
