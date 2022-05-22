package com.govtech.esm.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filterName = "";

	private String filterValue = "";

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSearchFilter other = (UserSearchFilter) obj;
		if (filterName == null) {
			if (other.filterName != null)
				return false;
		} else if (!filterName.equalsIgnoreCase(other.filterName))
			return false;

		if (filterValue == null) {
			if (other.filterValue != null)
				return false;
		} else if (!filterValue.equalsIgnoreCase(other.filterValue))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filterName == null) ? 0 : filterName.hashCode());
		result = prime * result + ((filterValue == null) ? 0 : filterValue.hashCode());
		return result;
	}

}
