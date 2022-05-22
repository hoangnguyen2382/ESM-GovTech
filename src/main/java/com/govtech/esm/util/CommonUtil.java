package com.govtech.esm.util;

import com.govtech.esm.exception.Errors;
import com.govtech.esm.exception.ServiceException;
import com.govtech.esm.vo.UserSearchFilter;
import com.govtech.esm.vo.UserSearchFilters;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.validation.Valid;

/**
 * This is Common util class.
 * 
 * @author vanhoang.nguyen
 *
 */
public final class CommonUtil {

	private CommonUtil() {

	}

	public static UserSearchFilters parseFilters(@Valid String filters) {

		UserSearchFilters userSearchFilters = null;
		if (filters != null && !filters.isEmpty()) {

			userSearchFilters = new UserSearchFilters();

			List<UserSearchFilter> userSearchFilterList = Collections.list(new StringTokenizer(filters, ",")).stream()
					.map(CommonUtil::stringToFilter)
					.collect(Collectors.toList());

			userSearchFilters.setFilters(userSearchFilterList);
		}

		return userSearchFilters;
	}

	private static UserSearchFilter stringToFilter(Object string) {

		StringTokenizer stringTokenizer = new StringTokenizer((String) string, "-");
		if (stringTokenizer.countTokens() != 2) {
			throw new ServiceException(Errors.USER_DATA_CRUD_FILTER_NOT_CORRECT_FORMAT, (String) string, null);
		}

		UserSearchFilter filter = new UserSearchFilter();
		filter.setFilterName(stringTokenizer.nextToken().trim().toLowerCase());
		filter.setFilterValue(stringTokenizer.nextToken().trim().toLowerCase());
		return filter;
	}
}