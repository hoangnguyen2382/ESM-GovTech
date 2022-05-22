package com.govtech.esm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.govtech.esm.model.ListUserResponse;
import com.govtech.esm.model.UserDetail;
import com.govtech.esm.model.UserResponse;
import com.govtech.esm.repository.UserDetailsRepository;
import com.govtech.esm.util.Constants;
import com.govtech.esm.util.DateUtil;

/**
 * RestControll Class to handle requests to user.
 * 
 * @author vanhoang.nguyen
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

	private final String startDate = DateUtil.format(new Date(), Constants.DATE_FORMAT_1);

	@Autowired
	private MockMvc mockMVC;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserDetailsRepository userRepository;

	@Test
	@DisplayName("Test Upload CSV - Success")
	@Order(1)
	public void testUpload() throws Exception {

		MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName", "text/csv",
				new ClassPathResource("userdetails_success.csv").getInputStream());

		mockMVC.perform(MockMvcRequestBuilders.multipart("/users/upload").file(fileName)).andExpect(status().isCreated());

		assertTrue(userRepository.findById("e0001").isPresent());
	}
	
	@Test
	@DisplayName("Test Upload CSV - Upload Success but No update")
	@Order(2)
	public void testUploadNoUpdate() throws Exception {

		MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName", "text/csv",
				new ClassPathResource("userdetails_success.csv").getInputStream());

		mockMVC.perform(MockMvcRequestBuilders.multipart("/users/upload").file(fileName)).andExpect(status().isOk());

	}
	
	@Test
	@DisplayName("Test Upload CSV - Success - Reupload With Changes")
	@Order(3)
	public void testUploadReuploadWithChanges() throws Exception {

		MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName", "text/csv",
				new ClassPathResource("userdetails_reupload_with_changes.csv").getInputStream());

		mockMVC.perform(MockMvcRequestBuilders.multipart("/users/upload").file(fileName)).andExpect(status().isCreated());

		assertTrue(userRepository.findById("e0001").isPresent());
	}
	
	

	@Test
	@DisplayName("Test Upload CSV - Invalid Salary-startdate")
	@Order(4)
	public void testUploadNoUpdateInvalidSalaryStartDate() throws Exception {

		MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName", "text/csv",
				new ClassPathResource("userdetails_invalid_salary_startdate.csv").getInputStream());

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.multipart("/users/upload").file(fileName)).andExpect(status().isBadRequest())
				.andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message(
				"Error in processing the file - Line 1:Salary in decimal that is >= 0.0 - (xxx). Line 2:Date of start of employment in one of two formats: 'yyyy-mm-dd' or 'dd-mmm-yy' - (2001-xxx-16). "),
				userResponse);
	}

	@Test
	@DisplayName("Test Upload CSV - Duplicated Rows")
	@Order(5)
	public void testUploadDuplicatedRows() throws Exception {

		MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName", "text/csv",
				new ClassPathResource("userdetails_invalid_duplicate.csv").getInputStream());

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.multipart("/users/upload").file(fileName)).andExpect(status().isBadRequest())
				.andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Error in processing the file - Line 2:Duplicated Records. "), userResponse);
	}

	@Test
	@DisplayName("Test Upload CSV - Login Unique Constraints")
	@Order(6)
	public void testUploadLoginUniqueConstraints() throws Exception {

		MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName", "text/csv",
				new ClassPathResource("userdetails_invalid_login_unique_constraints.csv").getInputStream());

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.multipart("/users/upload").file(fileName)).andExpect(status().isBadRequest())
				.andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Employee login not unique - (hpotter)"), userResponse);
	}

	@Test
	@DisplayName("Test Create User - Invalid Input - Salary Not Valid")
	@Order(10)
	public void testCreateUserInvalidInputSalary() throws Exception {

		assertFalse(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login_new").name("name_new").salary(-2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		// assertTrue(userResponse.getMessage().contains("Employee ID already exists"));
		assertEquals(new UserResponse().message("User Detail invalid - Salary must be at least 0"), userResponse);
	}

	@Test
	@DisplayName("Test Create User - Invalid Input - Login not unique")
	@Order(11)
	public void testCreateUserInvalidLoginnotUnique() throws Exception {

		assertFalse(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("hpotter").name("Harry Potter").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		// assertTrue(userResponse.getMessage().contains("Employee ID already exists"));
		assertEquals(new UserResponse().message("Employee login not unique - (hpotter)"), userResponse);
	}

	@Test
	@DisplayName("Test Create User - Invalid Input - StartDate not Valid")
	@Order(12)
	public void testCreateUserInvalidStartDateNotValid() throws Exception {

		assertFalse(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login1").name("name_new").salary(2000d).startDate("2021-50-11");

		MvcResult result = mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andDo(print())
				.andExpect(status().isBadRequest()).andReturn();
		
		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Invalid date - (2021-50-11)"), userResponse);
	}

	@Test
	@DisplayName("Test Create User - User Exist - Failed")
	@Order(13)
	public void testCreateUserUserExist() throws Exception {

		assertTrue(userRepository.findById("e0001").isPresent());

		UserDetail user = new UserDetail().id("e0001").login("login1").name("name1").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Employee ID already exists - (e0001)"), userResponse);
	}

	@Test
	@DisplayName("Test Create User - Success")
	@Order(14)
	public void testCreateUserUserNotExistSuccess() throws Exception {

		assertFalse(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login_new").name("name_new").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isCreated()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Successfully Created"), userResponse);

		// Reload usedetail from database to check if new user is created correctly
		com.govtech.esm.entity.UserDetail userDetailFromDB = userRepository.findById("id_new").get();
		assertEquals(user.getId(), userDetailFromDB.getId());
		assertEquals(user.getLogin(), userDetailFromDB.getLogin());
		assertEquals(user.getName(), userDetailFromDB.getName());
		assertEquals(user.getSalary().doubleValue(), userDetailFromDB.getSalary().doubleValue());
		// assertTrue(user.getStartDate().compareTo(userDetailFromDB.getStartDate())==0);
		// //TODO compare startDate
	}

	@Test
	@DisplayName("Test Update User - Invalid Input - Salary Not Valid")
	@Order(20)
	public void testUpdateUserInvalidInputSalary() throws Exception {

		assertTrue(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login_new").name("name_new").salary(-2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		// assertTrue(userResponse.getMessage().contains("Employee ID already exists"));
		assertEquals(new UserResponse().message("User Detail invalid - Salary must be at least 0"), userResponse);
	}

	@Test
	@DisplayName("Test Update User - Invalid Input - Login not unique")
	@Order(21)
	public void testUpdateUserInvalidLoginnotUnique() throws Exception {

		assertTrue(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("hpotter").name("name_new").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		// assertTrue(userResponse.getMessage().contains("Employee ID already exists"));
		assertEquals(new UserResponse().message("Employee login not unique - (hpotter)"), userResponse);
	}

	@Test
	@DisplayName("Test Update User - Invalid Input - StartDate not Valid")
	@Order(22)
	public void testUpdateUserInvalidStartDateNotValid() throws Exception {

		assertTrue(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login1").name("name_new").salary(2000d).startDate("2021-50-11");

		MvcResult result = mockMVC.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andDo(print())
				.andExpect(status().isBadRequest()).andReturn();
		
		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Invalid date - (2021-50-11)"), userResponse);

	}

	@Test
	@DisplayName("Test Update User - User Not Exist - Failed")
	@Order(23)
	public void testUpdateUserUserNotExist() throws Exception {

		assertFalse(userRepository.findById("id_new2").isPresent());

		UserDetail user = new UserDetail().id("id_new2").login("login_new").name("name_new").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("No such employee - (id_new2)"), userResponse);
	}

	@Test
	@DisplayName("Test Update User - Success")
	@Order(24)
	public void testUpdateUserUserSuccess() throws Exception {

		assertTrue(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login_new").name("name_new").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Successfully Updated"), userResponse);

		// Reload usedetail from database to check if new user is created correctly
		com.govtech.esm.entity.UserDetail userDetailFromDB = userRepository.findById("id_new").get();
		assertEquals(user.getId(), userDetailFromDB.getId());
		assertEquals(user.getLogin(), userDetailFromDB.getLogin());
		assertEquals(user.getName(), userDetailFromDB.getName());
		assertEquals(user.getSalary().doubleValue(), userDetailFromDB.getSalary().doubleValue());
		// assertTrue(user.getStartDate().compareTo(userDetailFromDB.getStartDate())==0);
		// //TODO compare startDate
	}

	@Test
	@DisplayName("Test getUser - User Exist")
	@Order(30)
	public void testGetUser_Exist() throws Exception {

		MvcResult result = mockMVC.perform(get("/users/id_new").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andReturn();
		String content = result.getResponse().getContentAsString();

		UserDetail user = objectMapper.readValue(content, UserDetail.class);
		assertEquals(new UserDetail().id("id_new").login("login_new").name("name_new").salary(2000d).startDate(startDate), user);
	}

	@Test
	@DisplayName("Test getUser - User Not Exist")
	@Order(31)
	public void testGetUser_NotExist() throws Exception {

		mockMVC.perform(get("/users/idxxxxx").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

	}

	@Test
	@DisplayName("Test Delete User - Success")
	@Order(40)
	public void testDeleteUserUserSuccess() throws Exception {

		assertTrue(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login_new").name("name_new").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC
				.perform(delete("/users/id_new").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("Successfully deleted"), userResponse);

		// Reload usedetail from database to check if new user is created correctly
		Optional<com.govtech.esm.entity.UserDetail> optional = userRepository.findById("id_new");

		assertFalse(optional.isPresent());
	}

	@Test
	@DisplayName("Test Delete User - User Not Exist")
	@Order(41)
	public void testDeleteUserUserNotExist() throws Exception {

		assertFalse(userRepository.findById("id_new").isPresent());

		UserDetail user = new UserDetail().id("id_new").login("login_new").name("name_new").salary(2000d).startDate(startDate);

		MvcResult result = mockMVC
				.perform(delete("/users/id_new").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertEquals(new UserResponse().message("No such employee - (id_new)"), userResponse);

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class TestSearchUsers {

		@BeforeEach
		public void init() throws Exception {
			// delete all data
			userRepository.deleteAll();

			// re-import  original data
			MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName", "text/csv",
					new ClassPathResource("userdetails_success.csv").getInputStream());

			mockMVC.perform(MockMvcRequestBuilders.multipart("/users/upload").file(fileName)).andExpect(status().isCreated());
		}

		@Test
		@DisplayName("Test search user - search salary range - Default input")
		@Order(50)
		public void testSearchUsersSearchDefaultInput() throws Exception {

			MvcResult result = mockMVC.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
					.andReturn();

			String content = result.getResponse().getContentAsString();
			ListUserResponse listUserResponse = objectMapper.readValue(content, ListUserResponse.class);

			List<UserDetail> results = listUserResponse.getResults();

			// Verify the sorting by ID, ASC
			// Verify return all with offset=0,limit=no limit
			assertEquals(6, results.size());
			assertEquals(new UserDetail().id("e0001").login("hpotter").name("Harry Potter").salary(1234.00).startDate("2001-11-16"), results.get(0));
			assertEquals(new UserDetail().id("e0010").login("basilisk").name("Basilisk").salary(23.43).startDate("2001-11-21"), results.get(5));

			// Recheck with java 8 filter. Salary in range[0,4000)
			List<UserDetail> list = results.stream()
					.filter((userDetail) -> (userDetail.getSalary().doubleValue() >= 0 && userDetail.getSalary().doubleValue() < 4000d))
					.collect(Collectors.toList());
			assertEquals(list.size(),results.size());
		}

		@Test
		@DisplayName("Test search user - search salary range - Default sort by ID - No filter")
		@Order(51)
		public void testSearchUsersSearchSalaryRange() throws Exception {

			MvcResult result = mockMVC.perform(
					get("/users?minSalary=3000&maxSalary=5000&offset=0&limit=0&sortName=id&sortDir=ASC").contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isOk()).andReturn();

			String content = result.getResponse().getContentAsString();
			ListUserResponse listUserResponse = objectMapper.readValue(content, ListUserResponse.class);

			List<UserDetail> results = listUserResponse.getResults();
			assertEquals(3, results.size());
			assertEquals(new UserDetail().id("e0003").login("ssnape").name("Severus Snape").salary(4000.0).startDate("2001-11-16"), results.get(0));

			// Recheck with java 8 filter
			List<UserDetail> list = results.stream()
					.filter((userDetail) -> (userDetail.getSalary().doubleValue() >= 3000d && userDetail.getSalary().doubleValue() < 5000d))
					.collect(Collectors.toList());
			assertEquals(3, list.size());
		}

		@Test
		@DisplayName("Test search user - offset - limit")
		@Order(52)
		public void testSearchUsersOffsetLimit() throws Exception {

			MvcResult result = mockMVC.perform(get("/users??offset=2&limit=2").contentType(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().isOk()).andReturn();

			String content = result.getResponse().getContentAsString();
			ListUserResponse listUserResponse = objectMapper.readValue(content, ListUserResponse.class);

			// Verify return from offset=2 and limit=2
			List<UserDetail> results = listUserResponse.getResults();
			assertEquals(2, results.size());
			assertEquals(new UserDetail().id("e0001").login("hpotter").name("Harry Potter").salary(1234.0).startDate("2001-11-16"), results.get(0));
			assertEquals(new UserDetail().id("e0004").login("rhagrid").name("Rubeus Hagrid").salary(3999.999).startDate("2001-11-16"),
					results.get(1));
		}

		@Test
		@DisplayName("Test search user - sort by fields- Asc")
		@Order(53)
		public void testSearchUsersSortAsc() throws Exception {

			MvcResult result = mockMVC.perform(get("/users?sortName=salary&sortDir=ASC").contentType(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().isOk()).andReturn();

			String content = result.getResponse().getContentAsString();
			ListUserResponse listUserResponse = objectMapper.readValue(content, ListUserResponse.class);

			// Verify sort by name
			List<UserDetail> results = listUserResponse.getResults();
			assertEquals(6, results.size());
			assertEquals(new UserDetail().id("e0007").login("hgranger").name("Hermione Granger").salary(0.0).startDate("2001-11-18"), results.get(0));
			assertEquals(new UserDetail().id("e0004").login("rhagrid").name("Rubeus Hagrid").salary(3999.999).startDate("2001-11-16"),
					results.get(5));

			// Recheck with java 8 filter
			List<UserDetail> list = results.stream().sorted(Comparator.comparingDouble(UserDetail::getSalary)).collect(Collectors.toList());
			assertEquals(new UserDetail().id("e0007").login("hgranger").name("Hermione Granger").salary(0.0).startDate("2001-11-18"), list.get(0));
			assertEquals(new UserDetail().id("e0004").login("rhagrid").name("Rubeus Hagrid").salary(3999.999).startDate("2001-11-16"), list.get(5));
		}

		@Test
		@DisplayName("Test search user - sort by fields- DESC")
		@Order(54)
		public void testSearchUsersSortDesc() throws Exception {

			MvcResult result = mockMVC.perform(get("/users?sortName=salary&sortDir=DESC").contentType(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().isOk()).andReturn();

			String content = result.getResponse().getContentAsString();
			ListUserResponse listUserResponse = objectMapper.readValue(content, ListUserResponse.class);

			// Verify sort by name
			List<UserDetail> results = listUserResponse.getResults();
			assertEquals(6, results.size());
			assertEquals(new UserDetail().id("e0004").login("rhagrid").name("Rubeus Hagrid").salary(3999.999).startDate("2001-11-16"),
					results.get(0));
			assertEquals(new UserDetail().id("e0007").login("hgranger").name("Hermione Granger").salary(0.0).startDate("2001-11-18"), results.get(5));

			// Recheck with java 8 filter
			List<UserDetail> list = results.stream().sorted(Comparator.comparingDouble(UserDetail::getSalary).reversed())
					.collect(Collectors.toList());
			assertEquals(new UserDetail().id("e0004").login("rhagrid").name("Rubeus Hagrid").salary(3999.999).startDate("2001-11-16"), list.get(0));
			assertEquals(new UserDetail().id("e0007").login("hgranger").name("Hermione Granger").salary(0.0).startDate("2001-11-18"), list.get(5));
		}

		@Test
		@DisplayName("Test search user - filters")
		@Order(55)
		public void testSearchUsersFilters() throws Exception {

			MvcResult result = mockMVC
					.perform(get("/users?minSalary=3000&maxSalary=5000&sortName=name&sortDir=ASC&offset=&limit=2&filters=id-e000,name-Harry")
							.contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isOk()).andReturn();

			String content = result.getResponse().getContentAsString();
			ListUserResponse listUserResponse = objectMapper.readValue(content, ListUserResponse.class);

			List<UserDetail> results = listUserResponse.getResults();

			// Recheck with java 8 filter
			List<UserDetail> list = results.stream()
					.filter((userDetail) -> (userDetail.getId().indexOf("e000") > -1 && userDetail.getName().indexOf("harry") > -1))
					.collect(Collectors.toList());
			assertEquals(list.size(),results.size());
		}

		@Test
		@DisplayName("Test search user - filters - incorrect Format")
		@Order(56)
		public void testSearchUsersFiltersIncorrectFormat() throws Exception {

			MvcResult result = mockMVC
					.perform(get("/users?minSalary=3000&maxSalary=5000&sortName=name&sortDir=ASC&offset=&limit=2&filters=id-e000,wrongfilter")
							.contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isBadRequest()).andReturn();

			UserResponse userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
			assertEquals(new UserResponse().message("Filter is not in correct format (name-value,name-value) - wrongfilter"), userResponse);
		}
	}
}
