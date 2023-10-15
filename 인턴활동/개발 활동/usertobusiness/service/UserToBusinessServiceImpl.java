package com.example.platform.domain.usertobusiness.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.platform.domain.admin.controller.AdminRestController;
import com.example.platform.domain.user.repo.UserRepository;
import com.example.platform.global.common.dto.SignupReqDto;
import com.example.platform.global.token.JwtProvider;
import com.example.platform.domain.usertobusiness.repo.UserToBusinessRepository;
import com.example.platform.domain.business.repo.BusinessRepository;
import com.example.platform.domain.usertobusiness.model.UserToBusiness;
import com.example.platform.global.util.Sha256;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
@RequiredArgsConstructor
public class UserToBusinessServiceImpl implements UserToBusinessService {
	private final UserToBusinessRepository u2brepo;
	private final BusinessRepository businessrepo;
	@Transactional(value = "jpaTxManager")
	@Override
	public void delete(String name) {
		u2brepo.deleteUserToBusiness(name);
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public void register(SignupReqDto data,String userId) {
		//사업이름list->사업id list
		List<Long> Id_list = businessrepo.convertNameToId(data.getBusiness_name());		
		//사용자에 할당된 사업 등록
		for(int i=0;i<Id_list.size();i++) {
			UserToBusiness u2b = UserToBusiness.builder()
								.userId(data.getUserId())
								.creater(userId)
								.issueCount((long)0)
								.businessId(Id_list.get(i))
								.isDeleted('N')
								.build();
			u2brepo.save(u2b);
		}
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public void modify(SignupReqDto data,String userId) {
		List<Long> Id_list = businessrepo.convertNameToId(data.getBusiness_name());
		String name = data.getUserId();
		//현재 할당된 사업을 제외하고 삭제
		u2brepo.modifyUserToBusiness(Id_list);
		//수정된 사업 등록
		for(int i=0;i<Id_list.size();i++) {
			UserToBusiness u2b = u2brepo.findUserToBusinessByUserAndBusiness(name, Id_list.get(i));
			//존재하지만 is_deleted = 'Y'인 것들은 is_deleted 만 수정
			//나머지는 등록
			if(u2b != null) {
				u2b.setIsDeleted('N');
				u2b.setUpdater(userId);
			}
			else {
				u2b = UserToBusiness.builder()
					.userId(data.getUserId())
					.creater(userId)
					.businessId(Id_list.get(i))
					.isDeleted('N')
					.build();
			}
			u2brepo.save(u2b);
		}
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public void deleteu2b(String hash_id,String userId) {
		//user가 삭제될 경우
		u2brepo.deleteU2b(hash_id,userId);
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public void deleteBusiness(long Id,String userId) {
		// business가 삭제될 경우
		u2brepo.deleteBusiness(Id,userId);
	}
}
