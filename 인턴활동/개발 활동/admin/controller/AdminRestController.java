package com.example.platform.domain.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.platform.domain.admin.service.AdminService;
import com.example.platform.domain.comment.service.CommentServiceImpl;
import com.example.platform.domain.user.service.UserService;
import com.example.platform.domain.business.service.BusinessService;
import com.example.platform.domain.code.service.CodeService;
import com.example.platform.domain.usertobusiness.service.UserToBusinessService;
import com.example.platform.domain.detector.service.DetectorService;
import com.example.platform.domain.user.model.User;
import com.example.platform.global.common.dto.BusinessListDto;
import com.example.platform.global.common.dto.BusinessRegReqDto;
import com.example.platform.global.common.dto.BusinessListResDto;
import com.example.platform.global.common.dto.CMRespDto;
import com.example.platform.global.common.dto.IssueInsReqDto;
import com.example.platform.global.config.auth.CustomUserDetails;
import com.example.platform.global.handler.error.ex.CustomApiException;
import com.example.platform.global.common.dto.UserListResDto;
import com.example.platform.global.common.dto.UserListReqDto;
import com.example.platform.global.common.dto.SignupReqDto;
import com.example.platform.global.common.dto.UserDto;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.util.StringUtils;

@Log4j2
@RestController
@RequiredArgsConstructor
public class AdminRestController {
	private final AdminService service;
	private final UserService user_service;
	private final BusinessService business_service;
	private final UserToBusinessService u2b_service;
	private final CodeService code_service;
	/* 사용자 리스트 조회 
	 * 
	 * */
	@GetMapping("/api/admin/users")
	public ResponseEntity<?> allUserList(@AuthenticationPrincipal CustomUserDetails user) {
		List<UserDto> userList;
		try {
			/* 토큰으로 사용자 id받아오기
			 * */
			String loginUserId = user.getUser().getUserId();
			/* 획득한 사용자 id로 사용자에게 할당된 사업 가져오기
			 * */
			List<String> myself = service.getMyBusinessList(loginUserId);
			/* 가져온 사업 리스트에 할당된 유저 리스트 가져오기
			 * */
			userList = service.getUsersList(myself);
		} catch (Exception e) {
			throw new CustomApiException("사용자 조회 실패");
		}

		return new ResponseEntity<>(new CMRespDto<>(1, "사용자 조회 성공", userList), HttpStatus.OK);

	}
	/* 사용자 본인 정보 조회
	 * */
	@GetMapping("/api/admin/users/myinfo")
	public ResponseEntity<?> myUserData(@AuthenticationPrincipal CustomUserDetails user) {
		List<UserDto> userList;
		try {
			/* 토큰으로 사용자id 가져오기
			 * */
			String loginUserId = user.getUser().getUserId();
			/* 가져온 사용자id로 본인 정보 조회가져오기
			 * */
			userList = service.getUserList(loginUserId);
		} catch (Exception e) {
			throw new CustomApiException("사용자 조회 실패");
		}

		return new ResponseEntity<>(new CMRespDto<>(1, "사용자 조회 성공", userList), HttpStatus.OK);

	}
	/*사용자 ID 중복 체크
	 **/
	@GetMapping("/api/admin/users/duplicate_check/{userId}")
	public ResponseEntity<?> id_Duplicate_Check(@PathVariable("userId") String userId) {
		String message; boolean check;
		CMRespDto cmp = new CMRespDto();
		try {
			/* 사용자 id 중복체크 */
			check = user_service.find_duplicate_id(userId);
		
			/* check = true 중복o
			 * check = false 중복x
			 * */
			if(check) {
				message = "중복된 ID가 있습니다.";
				cmp = new CMRespDto<>(-1,message,null);
			}else {
				message = "중복된 ID가 없습니다.";
				cmp = new CMRespDto<>(1,message,null);
			}
		} catch (Exception e) {
			throw new CustomApiException("중복 ID 조회 실패");
		}
		return new ResponseEntity<>(cmp, HttpStatus.OK);
	}
	/**
	 * 사용자 추가 
	*/
	@PostMapping("/api/admin/users")
	public ResponseEntity<?> createUser(@RequestBody SignupReqDto data,
										@AuthenticationPrincipal CustomUserDetails user) {
		try {
			//updater,creater 등록을 위한 id
			String userId = user.getUser().getUserId();
			/* user테이블에 사용자 등록
			 * */
			String check_result = user_service.register(data,userId);
			if(check_result.equals("실패"))throw new CustomApiException("사용자 등록 실패");
			/* user_to_business테이블에 사용자에 할당된 사업 등록
			 * */
			u2b_service.register(data,userId);
			
		} catch (Exception e) {
			log.error(e);
			throw new CustomApiException("사용자 등록 실패");
		}
		return new ResponseEntity<>(new CMRespDto<>(1, "사용자 등록 성공", null), HttpStatus.OK);
	}
	
	/**
	 * 사용자 수정
	*/
	@PutMapping("/api/admin/users")
	public ResponseEntity<?> modifyUser(@RequestBody SignupReqDto data,
										@AuthenticationPrincipal CustomUserDetails user) {
		try {	
			//updater,creater 등록을 위한 id
			String userId = user.getUser().getUserId();
			/* user테이블의 사용자 정보 수정
			 * */
			user_service.modify(data,userId);
			/* user_to_business테이블의 정보 수정
			 * */
			u2b_service.modify(data,userId);
		} catch (Exception e) {
			log.error(e);
			throw new CustomApiException("사용자 수정 실패");
		}
		return new ResponseEntity<>(new CMRespDto<>(1, "사용자 수정 성공", null), HttpStatus.OK);
	}
	
	
	/*사용자 삭제 
	 * */
	@DeleteMapping("/api/admin/users")
	public ResponseEntity<?> deleteUser(@RequestBody SignupReqDto data,
										@AuthenticationPrincipal CustomUserDetails user) {
		try {
			//updater,creater 등록을 위한 id
			String userId = user.getUser().getUserId();
			/* 사용자 id 가져오기
			 * */
			String Id = data.getUserId();
			/*암호화한 아이디*/
			String hash_id = user_service.deleteuser_first(Id,userId);
			/*u2b테이블에 삭제된 id 암호화된 id로 바꾸기
			 * */
			u2b_service.deleteu2b(hash_id,userId);
		} catch (Exception e) {
			log.error(e);
			throw new CustomApiException("사용자 삭제 실패");
		}

		return new ResponseEntity<>(new CMRespDto<>(1, "사용자 삭제 성공", null), HttpStatus.OK);
	}
	
	
	/* 사업 리스트 조회 
	 * */
	@GetMapping("/api/admin/business")
	public ResponseEntity<?> allBusinessList() {
		List<BusinessRegReqDto> BusinessList;
		try {
			/* 사업 리스트 조회
			 * */
			BusinessList = business_service.findBusinessList();
		} catch (Exception e) {
			throw new CustomApiException("사업 조회 실패");
		}
		return new ResponseEntity<>(new CMRespDto<>(1, "사업 조회 성공", BusinessList), HttpStatus.OK);
	}
	/* 사업 등록
	 * */
	@PostMapping("/api/admin/business")
	public ResponseEntity<?> createBusiness(@RequestBody BusinessRegReqDto data,
											@AuthenticationPrincipal CustomUserDetails user) {
		String userId = user.getUser().getUserId();
		try {
			//사업을 등록 & return값 = businessid 
			long businessId = business_service.register(data,userId);
			//code테이블에 정보 등록
			code_service.register(data,businessId);
		} catch (Exception e) {
			log.error(e);
			throw new CustomApiException("사업 등록 실패");
		}
		return new ResponseEntity<>(new CMRespDto<>(1, "사업 등록 성공", null), HttpStatus.OK);
	}
	
	/* 사업 수정
	 * */
	@PutMapping("/api/admin/business")
	public ResponseEntity<?> modifyBusiness(@RequestBody BusinessRegReqDto data,
											@AuthenticationPrincipal CustomUserDetails user) {
		try {
			//updater,creater 등록을 위한 id
			String userId = user.getUser().getUserId();
			//사업을 수정 & return = businessid
			long businessId = business_service.modify(data,userId);
			// code테이블에 수정했습니다.
			code_service.modify(data,businessId);
		} catch (Exception e) {
			log.error(e);
			throw new CustomApiException("사업 수정 실패");
		}
		return new ResponseEntity<>(new CMRespDto<>(1, "사업 수정 성공", null), HttpStatus.OK);
	}
	
	@DeleteMapping("/api/admin/business")
	public ResponseEntity<?> deleteBusiness(@RequestBody BusinessRegReqDto data,
											@AuthenticationPrincipal CustomUserDetails user) {
		try {
			//updater,creater 등록을 위한 id
			String userId = user.getUser().getUserId();
			//사업 삭제
			long businessId = business_service.delete(data);
			//issue_type삭제
			code_service.delete(businessId);
			//u2b에 있는 사업들 삭제
			u2b_service.deleteBusiness(businessId,userId);
		} catch (Exception e) {
			throw new CustomApiException("사업 삭제 실패");
		}

		return new ResponseEntity<>(new CMRespDto<>(1, "사업 삭제 성공", null), HttpStatus.OK);
	}
}
