package com.example.platform.global.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public interface UserListResDto {
	
	@JsonProperty(value = "userId")
	String getuser_id();
	
	@JsonProperty(value = "username")
	String getusername();
	
	@JsonProperty(value = "department")
	String getdepartment();
	
	@JsonProperty(value = "business_name")
	String getbusiness_name();
	
	@JsonProperty(value = "role")
	String getrole(); 
}
