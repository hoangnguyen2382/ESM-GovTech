package com.govtech.esm.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserSearch
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-05-21T21:34:35.900+08:00[Asia/Singapore]")

public class UserSearch   {
  @JsonProperty("minsalary")
  private Double minsalary;

  public UserSearch minsalary(Double minsalary) {
    this.minsalary = minsalary;
    return this;
  }

  /**
   * Get minsalary
   * @return minsalary
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Double getMinsalary() {
    return minsalary;
  }

  public void setMinsalary(Double minsalary) {
    this.minsalary = minsalary;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSearch userSearch = (UserSearch) o;
    return Objects.equals(this.minsalary, userSearch.minsalary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minsalary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSearch {\n");
    
    sb.append("    minsalary: ").append(toIndentedString(minsalary)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

