package com.example.platform.global.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter
@NoArgsConstructor
public class UserDto {
	
	@JsonProperty(value = "userId")
	private String userId;
	
	@JsonProperty(value = "username")
	private String userName;
	
	@JsonProperty(value = "department")
	private String department;
	
	@JsonProperty(value = "business_name")
	private List<String> businessName;
	
	@JsonProperty(value = "role")
	private String role; 
}