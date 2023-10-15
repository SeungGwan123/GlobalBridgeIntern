package com.example.platform.domain.usertobusiness.repo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.platform.domain.usertobusiness.model.UserToBusiness;
import com.example.platform.global.common.dto.BusinessListDto;

@Repository
public interface UserToBusinessRepository extends JpaRepository<UserToBusiness, Long> {

	List<UserToBusiness> findUserToBusinessByUserId(String userId);
	UserToBusiness findU2bByUserId(String userId);
	
	// jpa 속성을 쓰는게 아니라 HQL(hibernate 계층(?)을 쓰는듯 하여 클래스의 변수 명 들로 매핑함.
	@Query(value = "SELECT new com.example.platform.global.common.dto.BusinessListDto(A.businessId,"
			+ "A.businessName,A.startDate,A.endDate,A.intersectionCount,A.detectorCount,"
			+ "COUNT(CASE WHEN B.issueState in (2,3) THEN 1 END) as unsolvedIssue,"
			+ "COUNT(CASE WHEN B.issueState in (2,3) and B.partnerId = :userId THEN 1 END) as assignedIssue,"
			+ "A.issueSolvedCount as solvedIssue) "
			+ "FROM Business A " + "LEFT JOIN Issue B ON A.businessId= B.businessId "
			+ "WHERE A.businessId IN (SELECT businessId FROM UserToBusiness C WHERE C.userId = :userId) "
			+ "GROUP BY A.businessId " + "ORDER BY A.startDate ASC")
	List<BusinessListDto> findBusinessList(@Param("userId") String userId);
	
	@Query(value = "SELECT A.businessId as business_id,"
			+ "A.businessName as business_name ,A.startDate as start_date,A.endDate as start_date,"
			+ "A.intersectionCount as intersection_count,A.detectorCount as detector_count,"
			+ "COUNT(CASE WHEN B.issueState in (2,3) THEN 1 END) as unsolvedIssue,"
			+ "COUNT(CASE WHEN B.issueState = 1 and B.update_time BETWEEN (CURRENT_TIMESTAMP - 48) AND CURRENT_TIMESTAMP THEN 1 END) as solvedIssue,"
			+ "COUNT(CASE WHEN B.issueState in (2,3) and B.partnerId = :userId THEN 1 END) as assignedIssue "
			+ "FROM Business A " + "LEFT JOIN Issue B ON A.businessId = B.businessId "
			+ "WHERE A.businessId IN (SELECT businessId FROM UserToBusiness C WHERE C.userId = :userId) "
			+ "GROUP BY A.businessId " + "ORDER BY A.startDate ASC")
	List<Map<String,Object>> findBusinessListWithIssueInfo(@Param("userId") String userId);
	
	@Modifying
	@Query(value = "DELETE from user_to_business where user_id = :name",nativeQuery=true)
	void deleteUserToBusiness(@Param("name") String name);
	
	@Modifying
	@Query(value = "update user_to_business set is_deleted='Y', updater = :userId where user_id = :name",nativeQuery=true)
	void deleteU2b(@Param("name") String name,@Param("userId") String userId);
	
	@Modifying
	@Query(value = "update user_to_business set is_deleted='Y' where business_id not in :Id",nativeQuery=true)
	void modifyUserToBusiness(@Param("Id") List<Long> Id);
	
	@Modifying
	@Query(value = "update user_to_business b set is_deleted='Y', updater = :userId where b.business_id = :Id ",nativeQuery = true)
	void deleteBusiness(@Param("Id") long Id,@Param("userId") String userId);
	
	@Query(value = "select * from user_to_business where user_id = :user and business_id = :business",nativeQuery = true)
	UserToBusiness findUserToBusinessByUserAndBusiness(@Param("user") String user,@Param("business") long business);
}
