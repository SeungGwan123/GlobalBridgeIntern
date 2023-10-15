package com.example.platform.domain.business.repo;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.example.platform.global.common.dto.BusinessListResDto;

import com.example.platform.domain.business.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business,Integer>{
	Business findByBusinessId(Long businessId);
	Business findByBusinessName(String businessName);
	
	@Query(value = "select business_id from business "
			+ " where business_name = :name and start_date = :start",nativeQuery=true)
	int businessIdByData(@Param("name") String name,@Param("start") String start);
	
	@Query(value = "select b.business_id, b.business_name, b.start_date, b.end_date, Group_Concat(c.code_name) AS code_name ,Group_Concat(c.code_color) AS code_color "
				+ " from business b "
				+ " left join code c "
				+ " on b.business_id = c.business_id "
				+ " where b.is_deleted='N' "
				+ " group by b.business_id, b.business_name, b.start_date, b.end_date ",nativeQuery = true)
	List<BusinessListResDto> allBusinessList();
	
	@Modifying
	@Query(value = "update business b set b.is_deleted = 'Y' where b.business_id = :Id ",nativeQuery = true)
	void deleteBusiness(@Param("Id") long Id);
	
	@Query(value = " select b.business_id from business b where b.business_name in :name and b.is_deleted = 'N' ",nativeQuery=true )
	List<Long> convertNameToId(@Param("name") List<String> name);

}
