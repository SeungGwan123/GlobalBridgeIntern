package com.example.platform.domain.admin.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.platform.domain.business.model.Business;
import com.example.platform.domain.business.repo.BusinessRepository;
import com.example.platform.domain.detector.repo.DetectorRepository;
import com.example.platform.domain.intersection.repo.IntersectionRepository;
import com.example.platform.domain.user.repo.UserRepository;
import com.example.platform.domain.usertobusiness.repo.UserToBusinessRepository;
import com.example.platform.global.common.dto.BusinessListDto;
import com.example.platform.global.common.dto.UserDto;
import com.example.platform.global.handler.error.ex.CustomApiException;
import com.example.platform.global.common.dto.UserListResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
	private final UserRepository userRepository;
	/* user레포지토리에서 리스트를 받아오게 했습니다.*/
	@Transactional(value = "jpaTxManager")
	@Override
	public List<UserDto> getUsersList(List<String> business_list) {
		List<UserListResDto> temp_list=null;
		List<UserDto> list = new ArrayList<>();
		try {
			//유저 리스트 조회 -> Users
			temp_list = userRepository.findUsersListWithBusinessName(business_list);
			//String -> List<String>
			for(int i=0;i<temp_list.size();i++) {
				UserDto temp = new UserDto();
				temp.setUserId(temp_list.get(i).getuser_id());
				temp.setUserName(temp_list.get(i).getusername());
				temp.setRole(temp_list.get(i).getrole());
				temp.setDepartment(temp_list.get(i).getdepartment());
				String temp_business = temp_list.get(i).getbusiness_name();
				temp.setBusinessName(Arrays.asList(temp_business.split(",")));
				list.add(temp);
			}
		} catch (Exception e) {
			throw new CustomApiException("사용자 리스트 조회 불가");
		}
		return list;
	}
	
	/* user레포지토리에서 리스트를 받아오게 했습니다.*/
	@Transactional(value = "jpaTxManager")
	@Override
	public List<UserDto> getUserList(String user_id) {
		List<UserListResDto> temp_list=null;
		List<UserDto> list = new ArrayList<>();
		try {
			//유저 조회 -> User
			temp_list = userRepository.findUserListWithBusinessName(user_id);
			//String -> List<String>
			for(int i=0;i<temp_list.size();i++) {
				UserDto temp = new UserDto();
				temp.setUserId(temp_list.get(i).getuser_id());
				temp.setUserName(temp_list.get(i).getusername());
				temp.setRole(temp_list.get(i).getrole());
				temp.setDepartment(temp_list.get(i).getdepartment());
				String temp_business = temp_list.get(i).getbusiness_name();
				temp.setBusinessName(Arrays.asList(temp_business.split(",")));
				list.add(temp);
			}
		} catch (Exception e) {
			throw new CustomApiException("사용자 리스트 조회 불가");
		}
		return list;
	}
	
	/* user레포지토리에서 리스트를 받아오게 했습니다.*/
	@Transactional(value = "jpaTxManager")
	@Override
	public List<String> getMyBusinessList(String user_id) {
		List<String> list=null;
		try {
			//사업 리스트 조회
			String temp = userRepository.findBusinessListByUserId(user_id);
			//String -> List<String>
			list = Arrays.asList(temp.split(","));
		} catch (Exception e) {
			throw new CustomApiException("사용자 리스트 조회 불가");
		}
		return list;
	}
}
