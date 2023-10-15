package com.example.platform.domain.business.service;

import java.util.List;
import java.util.Map;

import com.example.platform.domain.business.model.Business;
import com.example.platform.global.common.dto.BusinessListDto;
import com.example.platform.global.common.dto.BusinessListResDto;
import com.example.platform.global.common.dto.BusinessRegReqDto;

public interface BusinessService {
	public abstract List<BusinessListDto> getMyBusinessList(String userId);

	public abstract void updateIntersectionCnt(long businessId);

	public abstract void updateDetectorCnt(long businessId);
	
	public List<BusinessRegReqDto> findBusinessList();
	
	public long register(BusinessRegReqDto data,String userId);
	
	public long delete(BusinessRegReqDto data);
	
	public long modify(BusinessRegReqDto data,String userId);
}
