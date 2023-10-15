package com.example.platform.domain.admin.service;

import java.util.List;
import java.util.Map;

import com.example.platform.domain.user.model.User;
import com.example.platform.global.common.dto.BusinessListDto;
import com.example.platform.global.common.dto.UserDto;
import com.example.platform.global.common.dto.UserListResDto;

public interface AdminService {
	public abstract List<UserDto> getUsersList(List<String> business_list);
	public List<UserDto> getUserList(String user_id);
	public List<String> getMyBusinessList(String user_id);
}
