#### System Overview
The project is to implement Employee Salary Management Rest API system using Spring Boot. These are frameworks and tools used in this application
- **SpringBoot-Starter Web**: Rest API
- **SpringBoot-Starter Data JPA**: Data repository
- **Spring Validation**: Validation
- **Lombok**: Easy the development
- **Apache Common-CSV**: Parse CSV File 
- **H2 Database**: Memory Database
- **SpringBoot-Start Test and Mockito**: Unit test
- **Swagger**: OpenAPI and swagger to define and expose the APIs
- **Jacoco**: Verify the code coverage

#### System Requirement
- Java 1.8 or above
- Maven

#### Assumption
- Upload CSV File: assume the file size to upload is less than 10MB.
- Employee's login is unique from database perspective, not only within a file.


#### Functions and Endpoints 

##### Upload User CSV
- Upload CSV: http://localhost:8080/users/upload - POST
  1. userdetails_success.csv: valid sample file
  2. userdetails_invalid_*.csv: invalid sample files

##### List Users (Search Salary Range, Sorting, Filters)
- Native query is used for complex functions.
- List Users: http://localhost:8080/users?minSalary=xxx&maxSalary=xxx&sortName=[id,login,name,salary,startDate]&sortDir=[ASC,DESC]&offset=&limit=&filters=filtername-filtervalue,filtername-filtervalue
- (E.g.: http://localhost:8080/users?minSalary=100&maxSalary=5000&sortName=name&sortDir=ASC&offset=&limit=2&filters=id-e000,name-H)


##### User CRUD 
- Get user: http://localhost:8080//users/{id} - GET
- Create: http://localhost:8080/users - POST
- Update: http://localhost:8080/users - PUT
- Delete: http://localhost:8080/users/{id} - DELETE

##### Swagger UI
- http://localhost:8080/swagger-ui.html

#### Run the program
- Run from Command line: `mvn compile exec:java`
- Run from Eclipse:
    1. Install the **Lombok** plugin to Eclipse
    2. Execute `mvn clean compile` to generate the source code from *swagger* file
    3. Start the EsmApplication

#### Run the test
The unit tests have been added to verify the implementation. The code coverage is more than 90% 
- Run the unit test: `mvn test`
- Verify the code coverage: `mvn test verify`
- Test report: target/site/jacoco/index.html

#### Suggestion
- If there is a need to process for big CSV file, please consider other file receiving channels such as SFTP and process backend with SpingBatch.
- We could generalize the API response with a CommonReponse for all APIs. CommonResponse will reflect: status, messages,...
- Adding security to secure APIs

