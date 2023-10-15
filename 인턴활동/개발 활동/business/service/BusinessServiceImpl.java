package com.example.platform.domain.business.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.platform.domain.business.model.Business;
import com.example.platform.domain.business.repo.BusinessRepository;
import com.example.platform.domain.detector.repo.DetectorRepository;
import com.example.platform.domain.intersection.repo.IntersectionRepository;
import com.example.platform.domain.issue.repo.IssueRepository;
import com.example.platform.domain.user.repo.UserRepository;
import com.example.platform.domain.usertobusiness.repo.UserToBusinessRepository;
import com.example.platform.global.common.dto.BusinessListDto;
import com.example.platform.global.common.dto.BusinessListResDto;
import com.example.platform.global.common.dto.BusinessRegReqDto;
import com.example.platform.global.handler.error.ex.CustomApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class BusinessServiceImpl implements BusinessService {
	// jpa repository
	private final BusinessRepository businessRepo;
	private final IntersectionRepository intersectionRepo;
	private final DetectorRepository detectorRepo;
	private final UserToBusinessRepository userToBusinessRepo;
	private final UserRepository userRepo;
	private final IssueRepository issueRepo;
	private final MessageSource messageSource;

	// mybatis dao
//	@Resource(name = "BusinessDao")
//	BusinessDao businessDao;

	@Transactional(value = "jpaTxManager")
	@Override
	public List<BusinessListDto> getMyBusinessList(String loginUserId) {

		List<BusinessListDto> result = new ArrayList<>();
		try {
			result = userToBusinessRepo.findBusinessList(loginUserId);
			result.forEach(r -> {
				// 할당 된 이슈 
				r.setAssigned_issue(issueRepo.countAssignedIssueCnt(r.getBusiness_id(), loginUserId));
				// 해결된 이슈 (48시간이내)
				r.setSolved_issue(issueRepo.countSolvedIssueCnt(r.getBusiness_id()));
			});

			log.debug("로그인한 {} 의 비즈니스 리스트 조회 성공.", loginUserId);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomApiException(messageSource.getMessage("common.read.error", new String[]{"사업"}, null));
		}

		return result;
	}

	@Transactional(value = "jpaTxManager", readOnly = true)
	@Override
	public List<BusinessRegReqDto> findBusinessList(){
		List<BusinessListResDto> temp_list;
		List<BusinessRegReqDto> list = new ArrayList<>();
		try {
			// 사업리스트 가져오기
			temp_list = businessRepo.allBusinessList();
			// String -> List<String>
			for(int i=0;i<temp_list.size();i++) {
				BusinessRegReqDto temp = new BusinessRegReqDto();
				temp.setBusinessId(temp_list.get(i).getbusiness_id());
				temp.setStartDate(temp_list.get(i).getstart_date());
				temp.setEndDate(temp_list.get(i).getend_date());
				temp.setBusinessName(temp_list.get(i).getbusiness_name());
				String temp_name = temp_list.get(i).getcode_name();
				if(temp_name!=null)	temp.setCodeName(Arrays.asList(temp_name.split(",")));
				//temp.setCodeName(Arrays.asList(temp_name.split(",")));
				String temp_color = temp_list.get(i).getcode_color();
				if(temp_color!=null) temp.setCodeColor(Arrays.asList(temp_color.split(",")));
				//temp.setCodeColor(Arrays.asList(temp_color.split(",")));
				list.add(temp);
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomApiException("사업 목록 조회 불가");
		}

		return list;
	}
	@Transactional(value = "jpaTxManager")
	@Override
	public void updateIntersectionCnt(long businessId) {
		try {
			Long cnt = intersectionRepo.countIntersectionsByBusinessId(businessId);
			log.debug("업데이트 될 교차로 개수 {} 개", cnt);
			Business business = businessRepo.findByBusinessId(businessId);
			business.setIntersectionCount(cnt.longValue());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomApiException("교차로 개수 업데이트 중 에러 발생");
		}

	}

	@Transactional(value = "jpaTxManager")
	@Override
	public void updateDetectorCnt(long businessId) {
		try {
			Long cnt = detectorRepo.countDetectorsByBusinessId(businessId);
			log.debug("업데이트 될 검지기 개수 {} 개", cnt);
			Business business = businessRepo.findByBusinessId(businessId);
			business.setDetectorCount(cnt.longValue());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomApiException("검지기 개수 업데이트 중 에러 발생");
		}
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public long register(BusinessRegReqDto data,String userId) {
		//사업 등록
		Business business = Business.builder()
							.businessName(data.getBusinessName())
							.startDate(data.getStartDate())
							.endDate(data.getEndDate())
							.activeStatus("Y")
							.creater(userId)
							.isDeleted('N')
							.build();
		businessRepo.save(business);
		long Id = business.getBusinessId();
		return Id;
	}
	
	
	@Transactional(value = "jpaTxManager")
	@Override
	public long modify(BusinessRegReqDto data,String userId) {		
		//사업 수정
		//원래 있던 사업 id로 불러와서 set으로 수정 후 save
        Business business = businessRepo.findByBusinessId(data.getBusinessId());
        	business.setBusinessName(data.getBusinessName());
        	business.setStartDate(data.getStartDate());
        	business.setEndDate(data.getEndDate());
        	business.setIsDeleted('N');
        	business.setUpdater(userId);
        	business.setActiveStatus("Y");
		businessRepo.save(business);
		
		return data.getBusinessId();
	}
	
	@Transactional(value = "jpaTxManager")
	@Override
	public long delete(BusinessRegReqDto data) {
		//사업 삭제
		businessRepo.deleteBusiness(data.getBusinessId());
        return data.getBusinessId();
	}
}
