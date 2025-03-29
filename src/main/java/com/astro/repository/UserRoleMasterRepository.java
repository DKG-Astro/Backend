package com.astro.repository;

import com.astro.entity.UserRoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleMasterRepository extends JpaRepository<UserRoleMaster, Integer> {
    UserRoleMaster findByRoleIdAndUserId(Integer roleId, Integer actionBy);
    UserRoleMaster findByUserId(Integer userId);

    boolean existsByRoleIdAndUserId(int i, Integer userId);


    @Query("SELECT ur.roleId FROM UserRoleMaster ur WHERE ur.userId = :userId")
    Optional<Integer> findRoleIdByUserId(@Param("userId") Integer userId);

}
