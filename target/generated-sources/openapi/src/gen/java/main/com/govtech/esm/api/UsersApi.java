/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.2.3).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.govtech.esm.api;

import com.govtech.esm.model.ListUserResponse;
import org.springframework.core.io.Resource;
import com.govtech.esm.model.UserDetail;
import com.govtech.esm.model.UserResponse;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-05-21T21:34:35.900+08:00[Asia/Singapore]")

@Validated
@Api(value = "users", description = "the users API")
public interface UsersApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /users : Create an user
     *
     * @param userDetail  (required)
     * @return New employee record created (status code 201)
     *         or Bad input - see error cases below (status code 400)
     *         or unexpected error (status code 200)
     */
    @ApiOperation(value = "Create an user", nickname = "createUser", notes = "", response = UserResponse.class, tags={ "UserDetail", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "New employee record created", response = UserResponse.class),
        @ApiResponse(code = 400, message = "Bad input - see error cases below"),
        @ApiResponse(code = 200, message = "unexpected error") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    default ResponseEntity<UserResponse> createUser(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UserDetail userDetail) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /users/{id} : Delete a specific user
     *
     * @param id The id of the user to delete (required)
     * @return Successfully deleted (status code 200)
     *         or Bad input - No such employee (status code 400)
     *         or unexpected error (status code 200)
     */
    @ApiOperation(value = "Delete a specific user", nickname = "deleteUser", notes = "", response = UserResponse.class, tags={ "UserDetail", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully deleted", response = UserResponse.class),
        @ApiResponse(code = 400, message = "Bad input - No such employee", response = UserResponse.class),
        @ApiResponse(code = 200, message = "unexpected error") })
    @RequestMapping(value = "/users/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    default ResponseEntity<UserResponse> deleteUser(@ApiParam(value = "The id of the user to delete",required=true) @PathVariable("id") String id) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /users/{id} : Get user detail
     *
     * @param id The id of the user to retrieve (required)
     * @return Success but no data updated (status code 200)
     *         or Bad input - no such employee (status code 400)
     *         or unexpected error (status code 200)
     */
    @ApiOperation(value = "Get user detail", nickname = "getUser", notes = "", response = UserDetail.class, tags={ "UserDetail", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success but no data updated", response = UserDetail.class),
        @ApiResponse(code = 400, message = "Bad input - no such employee"),
        @ApiResponse(code = 200, message = "unexpected error") })
    @RequestMapping(value = "/users/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<UserDetail> getUser(@ApiParam(value = "The id of the user to retrieve",required=true) @PathVariable("id") String id) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"name\" : \"name\", \"id\" : \"id\", \"login\" : \"login\", \"salary\" : 0.8008281904610115, \"startDate\" : \"2022-05-15\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /users : List users
     *
     * @param minSalary  (optional, default to 0)
     * @param maxSalary  (optional, default to 4000)
     * @param offset  (optional, default to 0)
     * @param limit  (optional, default to 0)
     * @param sortName  (optional, default to id)
     * @param sortDir  (optional, default to ASC)
     * @param filters  (optional)
     * @return An array of users (status code 200)
     *         or unexpected error (status code 200)
     */
    @ApiOperation(value = "List users", nickname = "listUsers", notes = "", response = ListUserResponse.class, tags={ "UserDetail", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "An array of users", response = ListUserResponse.class),
        @ApiResponse(code = 200, message = "unexpected error") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<ListUserResponse> listUsers(@ApiParam(value = "", defaultValue = "0") @Valid @RequestParam(value = "minSalary", required = false, defaultValue="0") Double minSalary,@ApiParam(value = "", defaultValue = "4000") @Valid @RequestParam(value = "maxSalary", required = false, defaultValue="4000") Double maxSalary,@ApiParam(value = "", defaultValue = "0") @Valid @RequestParam(value = "offset", required = false, defaultValue="0") Integer offset,@ApiParam(value = "", defaultValue = "0") @Valid @RequestParam(value = "limit", required = false, defaultValue="0") Integer limit,@ApiParam(value = "", allowableValues = "id, login, name, salary, startDate", defaultValue = "id") @Valid @RequestParam(value = "sortName", required = false, defaultValue="id") String sortName,@ApiParam(value = "", allowableValues = "ASC, DESC", defaultValue = "ASC") @Valid @RequestParam(value = "sortDir", required = false, defaultValue="ASC") String sortDir,@ApiParam(value = "") @Valid @RequestParam(value = "filters", required = false) String filters) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"results\" : [ { \"name\" : \"name\", \"id\" : \"id\", \"login\" : \"login\", \"salary\" : 0.8008281904610115, \"startDate\" : \"2022-05-15\" }, { \"name\" : \"name\", \"id\" : \"id\", \"login\" : \"login\", \"salary\" : 0.8008281904610115, \"startDate\" : \"2022-05-15\" } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /users : Update an user
     *
     * @param userDetail  (required)
     * @return Successfully updated. (status code 200)
     *         or Bad input - no such employee, login not unique etc. (status code 400)
     *         or unexpected error (status code 200)
     */
    @ApiOperation(value = "Update an user", nickname = "updateUser", notes = "", response = UserResponse.class, tags={ "UserDetail", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully updated.", response = UserResponse.class),
        @ApiResponse(code = 400, message = "Bad input - no such employee, login not unique etc.", response = UserResponse.class),
        @ApiResponse(code = 200, message = "unexpected error") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    default ResponseEntity<UserResponse> updateUser(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UserDetail userDetail) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"message\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /users/upload : Upload User CSV
     *
     * @param fileName  (optional)
     * @return Success but no data updated (status code 200)
     *         or Data created or uploaded (status code 201)
     *         or Bad input - parsing error, duplicate row, invalid salary etc. (status code 400)
     *         or unexpected error (status code 200)
     */
    @ApiOperation(value = "Upload User CSV", nickname = "upload", notes = "", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success but no data updated"),
        @ApiResponse(code = 201, message = "Data created or uploaded"),
        @ApiResponse(code = 400, message = "Bad input - parsing error, duplicate row, invalid salary etc."),
        @ApiResponse(code = 200, message = "unexpected error") })
    @RequestMapping(value = "/users/upload",
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    default ResponseEntity<Void> upload(@ApiParam(value = "") @Valid @RequestPart("fileName") MultipartFile fileName) throws Exception {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
