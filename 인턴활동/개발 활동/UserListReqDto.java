package com.example.platform.global.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserListReqDto {
	@JsonProperty(value="business_names")
	private List<String> business_list;
}
