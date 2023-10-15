package com.example.platform.global.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BusinessRegReqDto {
	@JsonProperty(value = "business_id")
	private long businessId;
	@JsonProperty(value = "business_name")
	private String businessName;
	@JsonProperty(value = "start_date")
	private String startDate;
	@JsonProperty(value = "end_date")
	private String endDate;
	@JsonProperty("code_name")
	private List<String> codeName;
	@JsonProperty("code_color")
	private List<String> codeColor;
}
