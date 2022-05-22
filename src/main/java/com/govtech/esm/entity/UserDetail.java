package com.govtech.esm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "User_Detail")
@Data
public class UserDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@NotBlank(message = "Id cannot be blank")
	private String id;

	@Column(name = "login")
	@NotBlank(message = "Login cannot be blank")
	private String login;

	@Column(name = "name")
	@NotBlank(message = "Name cannot be blank")
	private String name;

	@Column(name = "salary")
	@NotNull(message = "Salary cannot be blank")
	@Min(value = 0, message = "Salary must be at least {value}")
	private BigDecimal salary;

	@Column(name = "startDate")
	@Temporal(TemporalType.DATE)
	@NotNull(message = "startDate cannot be blank")
	private Date startDate;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		UserDetail other = (UserDetail) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(login, other.login)
				&& Objects.equals(name, other.name)
				&& Objects.equals(salary.doubleValue(), other.salary.doubleValue())
				&& Objects.equals(startDate, other.startDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, login, name, salary, startDate);
	}

}
