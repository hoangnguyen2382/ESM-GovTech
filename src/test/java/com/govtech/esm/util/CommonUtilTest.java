package com.govtech.esm.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.StringTokenizer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.govtech.esm.exception.Errors;
import com.govtech.esm.exception.ServiceException;
import com.govtech.esm.vo.UserSearchFilter;
import com.govtech.esm.vo.UserSearchFilters;

public class CommonUtilTest {

	private UserSearchFilter userSearchFilter1 = ObjectBuilder.createUserSearchFilter("login", "loginValue1");

	private UserSearchFilter userSearchFilter2 = ObjectBuilder.createUserSearchFilter("name", "nameValue1");

	public CommonUtilTest() {
		super();
	}

	@ParameterizedTest
	@DisplayName("Test parseFilters with valid input")
	@ValueSource(strings = { "login-loginValue1,name-nameValue1" })
	@Order(1)
	public void parseFilters_Valid(String filtersString) {

		UserSearchFilters filters = CommonUtil.parseFilters(filtersString);

		assertNotNull(filters);

		assertEquals(2, filters.getFilters().size());

		assertEquals(userSearchFilter1, filters.getFilters().get(0));
		assertEquals(userSearchFilter2, filters.getFilters().get(1));
	}

	@ParameterizedTest
	@DisplayName("Test parseFilters with empty")
	@ValueSource(strings = { "login-loginValue1," })
	@Order(2)
	public void parseFilters_Valid_Empty(String filtersString) {

		UserSearchFilters filters = CommonUtil.parseFilters(filtersString);

		assertNotNull(filters);

		assertEquals(1, filters.getFilters().size());

		assertEquals(userSearchFilter1, filters.getFilters().get(0));

	}

	@ParameterizedTest
	@DisplayName("Test parseFilters with invalid input")
	@ValueSource(strings = { "login-login,somestring" })
	@Order(3)
	public void parseFilters_InvalidFormat_ThrowException(String filtersString) {

		ServiceException exception = assertThrows(ServiceException.class, () -> CommonUtil.parseFilters(filtersString));

		assertEquals(Errors.USER_DATA_CRUD_FILTER_NOT_CORRECT_FORMAT, exception.getError());
	}

	private static UserSearchFilter stringToFilter(Object string) {

		UserSearchFilter filter = new UserSearchFilter();

		StringTokenizer stringTokenizer = new StringTokenizer((String) string, "-");

		if (stringTokenizer.countTokens() == 2) {
			filter.setFilterName(stringTokenizer.nextToken().trim().toLowerCase());
			filter.setFilterValue(stringTokenizer.nextToken().trim().toLowerCase());
		}

		return filter;
	}
}