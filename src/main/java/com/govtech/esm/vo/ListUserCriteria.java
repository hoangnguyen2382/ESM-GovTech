package com.govtech.esm.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListUserCriteria implements Serializable {

	private static final long serialVersionUID = 4946343335601512014L;

	private BigDecimal minSalary;

	private BigDecimal maxSalary;

	private int offset;

	private int limit;

	private String sortName;

	private String sortDir;

	private UserSearchFilters userSearchFilters;
}
