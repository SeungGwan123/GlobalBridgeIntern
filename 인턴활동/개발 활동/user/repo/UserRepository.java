package com.example.platform.domain.user.repo;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.platform.global.common.dto.UserListResDto;

import com.example.platform.domain.user.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
	User findByUserId(String userId);

	User findByUsername(String userName);
	// User findUserBusiness(String userId);

	@Modifying
	@Query(value = "UPDATE user set refreshToken = :refreshToken where user_id = :userId", nativeQuery = true)
	int updateRefreshToken(@Param("userId") String userId, @Param("refreshToken") String refreshToken) throws Exception;

	@Query(value = "select u.user_id, u.username, u.department, Group_concat(b.business_name order by b.business_name) AS business_name, u.role "
					+ " from user u, user_to_business a, business b "
					+ " where u.user_id = a.user_id and a.business_id = b.business_id and b.business_name in :business_list and u.is_deleted='N' "
					+ " group by u.user_id, u.username, u.department, u.role "
					+ " order by u.role ASC , u.create_time DESC ", nativeQuery=true)
	List<UserListResDto> findUsersListWithBusinessName(@Param("business_list") List<String> business_list);
	
	@Query(value = "select u.user_id, u.username, u.department, Group_concat(b.business_name order by b.business_name) AS business_name, u.role "
			+" from user u, user_to_business a, business b "
			+" where u.user_id = a.user_id and a.business_id = b.business_id and u.user_id = :userId "
			+ " group by u.user_id, u.username, u.department, u.role ", nativeQuery=true)
	List<UserListResDto> findUserListWithBusinessName(@Param("userId") String userId);
	
	@Query(value = "select Group_concat(b.business_name order by b.business_name) AS business_name "
			+" from user_to_business a, business b "
			+" where a.business_id = b.business_id and a.user_id = :userId ", nativeQuery=true)
	String findBusinessListByUserId(@Param("userId") String userId);
	
	@Query(value = "select user_id from user where user_id = :name",nativeQuery=true)
	String duplicateCheck(@Param("name") String name);
	
	@Modifying
	@Query(value = "delete from user where user_id = :name",nativeQuery=true)
	void deleteUserId(@Param("name") String name);
	
	@Modifying
	@Query(value = "UPDATE user SET user_id = :new_id , is_deleted = 'Y' , updater = :userId where user_id = :name ",nativeQuery=true)
	void isDeleteUserId(@Param("name") String name,@Param("new_id") String new_id,@Param("userId") String userId);
}
