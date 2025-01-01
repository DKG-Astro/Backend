package com.astro.repository;

import com.astro.entity.UserRoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMasterRepository extends JpaRepository<UserRoleMaster, Integer> {
}
