package com.example.platform.domain.usertobusiness.service;

import com.example.platform.global.common.dto.SignupReqDto;

public interface UserToBusinessService {
	public void delete(String name);
	
	public void register(SignupReqDto data,String userId);
	
	public void modify(SignupReqDto data,String userId);
	
	public void deleteu2b(String hash_id,String userId);
	
	public void deleteBusiness(long Id,String userId);
}
