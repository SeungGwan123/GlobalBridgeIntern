package com.example.platform.domain.user.service;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.*;
import com.example.platform.domain.user.model.User;
import com.example.platform.domain.user.repo.UserRepository;
import com.example.platform.global.common.dto.CMRespDto;
import com.example.platform.global.common.dto.SignupReqDto;
import com.example.platform.global.common.dto.SignupResDto;
import com.example.platform.global.util.Sha256;
import com.example.platform.global.handler.error.ex.CustomApiException;
import com.example.platform.global.token.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final MessageSource messageSource;

	@Transactional("jpaTxManager")
	@Override
	public CMRespDto<List<Map<String, String>>> signIn(SignupReqDto req) {

		String result = "로그인 성공";

		CMRespDto<List<Map<String, String>>> cmrespDto = new CMRespDto<>();
		List dataList = new ArrayList<Map<String, String>>();

		User user = userRepository.findByUserId(req.getUserId());

		if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
			throw new CustomApiException(messageSource.getMessage("authentication.pwIdWrong.fail", null, null));

		}

		// refreshtoken DB에 UPDATE
		int result2 = 0;
		try {
			result2 = userRepository.updateRefreshToken(user.getUserId(), jwtProvider.createRefreshToken());

			log.info(req.getUserId() + " 로그인 완료");

			log.debug(req.getUserId()+" 로그인 완료");

		} catch (Exception e) {

			if (result2 != 1) {
				System.out.println("refresh토큰 저장 실패");
			}
		}

		HashMap<String, String> dataMap = new HashMap<String, String>();

		// accesstoken 생성
		dataMap.put("AccessToken", jwtProvider.createAccessToken(user.getUserId(), user.getRole()));
		dataMap.put("role", user.getRole());
		dataMap.put("username", user.getUsername());
		dataMap.put("user_id", user.getUserId());

		dataList.add(dataMap);
		cmrespDto.setCode(1);
		cmrespDto.setMessage(result);
		cmrespDto.setData(dataList);

		return cmrespDto;
	}
	@Transactional(value = "jpaTxManager")
	@Override
	public String register(SignupReqDto req, String userId) {
		
		String result = "성공";
		//유저 등록
		User user = User.builder().userId(req.getUserId())
				.password(passwordEncoder.encode(req.getPassword()))
				.username(req.getUsername())
				.role(req.getRole())
				.creater(userId)
				.department(req.getDepartment())
				.isDeleted('N')
				.build();
		userRepository.save(user);
		//등록 여부 검사
		if(userRepository.findByUserId(req.getUserId())==null) result = "실패";
		return result;
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public void modify(SignupReqDto req, String userId) {
		//사용자 수정
		User user = userRepository.findByUserId(req.getUserId());
		if(!req.getPassword().equals(""))user.setPassword(passwordEncoder.encode(req.getPassword()));
			user.setUsername(req.getUsername());
			user.setRole(req.getRole());
			user.setUpdater(userId);
			user.setDepartment(req.getDepartment());
			user.setIsDeleted('N');
		userRepository.save(user);
		
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public SignupResDto getMember(String userId) {
		User user = userRepository.findByUserId(userId);
		return new SignupResDto(user);
	}

	public boolean find_duplicate_id(String name) {
		boolean result;
		//중복체크
		String check = userRepository.duplicateCheck(name);
		//check == name -> 중복 o
		//check != name -> 중복 x
		if(name.equals(check)) {
			result = true;
		}else {
			result = false;
		}
		return result;
	}
	@Transactional(value = "jpaTxManager")
	@Override
	public String deleteuser_first(String Id,String userId) {
		
		User user = userRepository.findByUserId(Id);
		//user_id sha256 방식으로 암호화
		Timestamp create_time = user.getCreate_time();
		String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(create_time);
		String new_id = Sha256.encrypt(Id+time).substring(0, 9);
		//user테이블에서 삭제(is_deleted = 'Y' user_id = new_id로 변환)
		userRepository.isDeleteUserId(Id,new_id,userId);
		return new_id;
	}
}
