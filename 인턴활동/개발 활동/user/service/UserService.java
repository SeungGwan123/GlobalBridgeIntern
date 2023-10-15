package com.example.platform.domain.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.platform.global.common.dto.CMRespDto;
import com.example.platform.global.common.dto.SignupReqDto;
import com.example.platform.global.common.dto.SignupResDto;


@Service
public interface UserService {

	/* 사용자 로그인 */
	public CMRespDto<List<Map<String, String>>> signIn(SignupReqDto signUpReqdto);

	/* 회원가입 */
	public String register(SignupReqDto signUpReqDto,String userId);

	/* 회원체크 */
	public SignupResDto getMember(String userId);

	/* ID중복체크 */
	public boolean find_duplicate_id(String name);
	
	public void modify(SignupReqDto req, String userId);
	
	public String deleteuser_first(String Id,String userId);
	
}
