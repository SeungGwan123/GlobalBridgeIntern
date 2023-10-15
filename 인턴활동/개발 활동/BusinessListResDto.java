package com.example.platform.global.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface BusinessListResDto {
	@JsonProperty(value = "business_id")
	long getbusiness_id();
	@JsonProperty(value = "business_name")
	String getbusiness_name();
	@JsonProperty(value = "start_date")
	String getstart_date();
	@JsonProperty(value = "end_date")
	String getend_date();
	@JsonProperty("code_name")
	String getcode_name();
	@JsonProperty("code_color")
	String getcode_color();
}
