package com.example.platform.global.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusinessListDto {

	@JsonProperty(value = "business_id")
	private Long business_id;
	@JsonProperty(value = "business_name")
	private String business_name;
	@JsonProperty(value = "start_date")
	private String start_date;
	@JsonProperty(value = "end_date")
	private String end_date;
	@JsonProperty(value = "intersection_count")
	private Long intersection_count;
	@JsonProperty(value = "detector_count")
	private Long detector_count;

	@Setter
	@JsonProperty(value = "unsolved_issue")
	private Long unsolved_issue; // 전체 미해결 이슈
	
	@JsonProperty(value = "solved_issue")
	private Long solved_issue; // 전체 해결이슈
	@Setter
	@JsonProperty(value = "assigned_issue")
	private Long assigned_issue; // 할당 된 이슈(로그인 한 사용자)

}
