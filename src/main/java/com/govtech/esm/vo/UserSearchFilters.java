package com.govtech.esm.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSearchFilters {
	private List<UserSearchFilter> filters = null;

	public UserSearchFilters filters(List<UserSearchFilter> filters) {
		this.filters = filters;
		return this;
	}

}
