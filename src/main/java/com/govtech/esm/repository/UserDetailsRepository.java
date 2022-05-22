package com.govtech.esm.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.govtech.esm.entity.UserDetail;

/**
 * User_Detail Repository.
 * 
 * @author vanhoang.nguyen
 *
 */
public interface UserDetailsRepository extends JpaRepository<UserDetail, String> {

	Optional<UserDetail> findByLogin(String login);

	@Query(value = "SELECT id, " + 
			"         login, " + 
			"         name, " + 
			"         salary, " + 
			"         start_date " + 
			"    FROM USER_DETAIL " + 
			"   WHERE     salary >= :minSalary " + 
			"         AND salary < :maxSalary " + 
			"         AND ( :idFilter IS NULL OR LOWER (id) LIKE '%' || :idFilter || '%') " + 
			"         AND ( :loginFilter IS NULL OR LOWER (login) LIKE '%' || :loginFilter || '%') " + 
			"         AND ( :nameFilter IS NULL OR LOWER (name) LIKE '%' || :nameFilter || '%') " + 
			"         AND ( :salaryFilter IS NULL OR salary = :salaryFilter) " + 
			"         AND ( :startDateFilter IS NULL OR TRUNC (start_date) = TRUNC ( :startDateFilter)) " + 
			"ORDER BY :sortIndex ASC  limit :offset, :limit  ", nativeQuery = true)
	List<UserDetail> findUsersAsc(BigDecimal minSalary, BigDecimal maxSalary, String idFilter, String loginFilter,
			String nameFilter, BigDecimal salaryFilter, Date startDateFilter, int sortIndex, int offset, int limit);

	@Query(value = "SELECT id, " + 
			"         login, " + 
			"         name, " + 
			"         salary, " + 
			"         start_date " + 
			"    FROM USER_DETAIL " + 
			"   WHERE     salary >= :minSalary " + 
			"         AND salary < :maxSalary " + 
			"         AND ( :idFilter IS NULL OR LOWER (id) LIKE '%' || :idFilter || '%') " + 
			"         AND ( :loginFilter IS NULL OR LOWER (login) LIKE '%' || :loginFilter || '%') " + 
			"         AND ( :nameFilter IS NULL OR LOWER (name) LIKE '%' || :nameFilter || '%') " + 
			"         AND ( :salaryFilter IS NULL OR salary = :salaryFilter) " + 
			"         AND ( :startDateFilter IS NULL OR TRUNC (start_date) = TRUNC ( :startDateFilter)) " + 
			"ORDER BY :sortIndex DESC  limit :offset, :limit  ", nativeQuery = true)
	List<UserDetail> findUsersDesc(BigDecimal minSalary, BigDecimal maxSalary, String idFilter, String loginFilter,
			String nameFilter, BigDecimal salaryFilter, Date startDateFilter, int sortIndex, int offset, int limit);
}